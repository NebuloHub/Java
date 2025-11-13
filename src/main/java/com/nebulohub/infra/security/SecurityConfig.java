// File: src/main/java/com/nebulohub/infra/security/SecurityConfig.java (UPDATED)
package com.nebulohub.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nebulohub.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    // ObjectMapper needs JavaTimeModule to serialize 'Instant' in ErrorResponse
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * **API Security Chain (Order 1)**
     * This chain handles all stateless API requests (/api/**)
     * It uses our custom JWT SecurityFilter.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                // This chain only applies to paths starting with /api/, /swagger-ui/, or /v3/api-docs/
                .securityMatcher("/api/**", "/swagger-ui/**", "/v3/api-docs/**")
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless API
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public API endpoints
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ratings/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // All other API requests must be authenticated
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        // Handle API 401 Unauthorized
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            ErrorResponse errorResponse = new ErrorResponse(
                                    HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
                                    "You must be authenticated to access this resource.", request.getRequestURI()
                            );
                            objectMapper.writeValue(response.getOutputStream(), errorResponse);
                        })
                        // Handle API 403 Forbidden
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            ErrorResponse errorResponse = new ErrorResponse(
                                    HttpStatus.FORBIDDEN.value(), "Forbidden",
                                    "You do not have permission to access this resource.", request.getRequestURI()
                            );
                            objectMapper.writeValue(response.getOutputStream(), errorResponse);
                        })
                )
                // Add our custom JWT filter
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * **Web Security Chain (Order 2 - Default)**
     * This chain handles all stateful (session-based) web requests for Thymeleaf.
     * It uses standard formLogin and sessions.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                // This chain applies to ALL other requests that API chain didn't catch
                .authorizeHttpRequests(auth -> auth
                        // Public web pages and static resources
                        // We use simple string matchers, no deprecated AntPathRequestMatcher
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register", // We need to create this page later
                                "/posts",
                                "/posts/**",
                                "/css/**",
                                "/images/**",
                                "/error/**", // Allow error pages
                                "/favicon.ico"
                        ).permitAll()
                        // All other web pages require authentication
                        .anyRequest().authenticated()
                )
                // --- Form Login Config for Thymeleaf ---
                .formLogin(form -> form
                        .loginPage("/login") // Use our custom login page
                        .loginProcessingUrl("/login") // The URL to submit the POST form to
                        .defaultSuccessUrl("/posts?loginSuccess", true) // Redirect to posts on success
                        .failureUrl("/login?error") // Redirect back on failure
                        .permitAll()
                )
                // --- Logout Config for Thymeleaf ---
                .logout(logout -> logout
                        .logoutRequestMatcher(request -> request.getMethod().equals("POST") && request.getServletPath().equals("/logout")) // Modern replacement

                        // --- THIS IS THE FIX ---
                        .logoutSuccessUrl("/posts?logout") // <-- CHANGED from "/login?logout"

                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        // Handle Web 403 Forbidden
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/login?forbidden");
                        })
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}