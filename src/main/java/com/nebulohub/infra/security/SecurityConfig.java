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
                .securityMatcher("/api/**", "/swagger-ui/**", "/v3/api-docs/**")
                .csrf(AbstractHttpConfigurer::disable)
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
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            ErrorResponse errorResponse = new ErrorResponse(
                                    HttpStatus.UNAUTHORIZED.value(), "Unauthorized",
                                    "You must be authenticated to access this resource.", request.getRequestURI()
                            );
                            objectMapper.writeValue(response.getOutputStream(), errorResponse);
                        })
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
                .authorizeHttpRequests(auth -> auth
                        // Public static resources
                        .requestMatchers(
                                "/css/**",
                                "/images/**",
                                "/error/**",
                                "/favicon.ico"
                        ).permitAll()

                        // Public web pages
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register" // We need to create this page later
                        ).permitAll()

                        // ** THE FIX **
                        // Explicitly permit GET to /posts and /posts/{id-with-digits}
                        .requestMatchers(HttpMethod.GET, "/posts", "/posts/{id:[0-9]+}").permitAll()

                        // All other web pages require authentication
                        .anyRequest().authenticated()
                )
                // --- Form Login Config for Thymeleaf ---
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/posts?loginSuccess", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                // --- Logout Config for Thymeleaf ---
                .logout(logout -> logout
                        .logoutRequestMatcher(request -> request.getMethod().equals("POST") && request.getServletPath().equals("/logout"))
                        .logoutSuccessUrl("/posts?logout") // Redirect to posts, not login
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
                // We DO NOT add the SecurityFilter here, this chain uses sessions.
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}