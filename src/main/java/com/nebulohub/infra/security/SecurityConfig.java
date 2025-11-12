package com.nebulohub.infra.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * CONTROLE DE SEGURANÇA
 *
 * - ENDPOINTS DE API --> /api/
 * - MVC -> /
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final CookieBlacklistLogoutHandler cookieLogoutHandler;

    public SecurityConfig(SecurityFilter securityFilter,
                          CookieBlacklistLogoutHandler cookieLogoutHandler) {
        this.securityFilter = securityFilter;
        this.cookieLogoutHandler = cookieLogoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // REQUISIÇÕES WEB MANDARÃO O USUÁRIO PARA UM REDIRECT DE LOGIN;
                // REQUISIÇÕES DIRETAMENTE NA API RETORNARÃO 401 UNAUTHORIZED;
                // EXCEPTION DE AUTENTICAÇÃO
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, authEx) -> {
                            String accept = req.getHeader("Accept");
                            if (accept != null && accept.contains("text/html")) {
                                String redirectTo = req.getRequestURI();
                                res.sendRedirect("/login?redirectTo=" + java.net.URLEncoder.encode(redirectTo, java.nio.charset.StandardCharsets.UTF_8));
                            } else {
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                            }
                        })
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )

                // ADICIONANDO O FILTRO DE SEGURANÇA ANTES DAS REQUISIÇOES
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                // PASSANDO 'EMAIL' COMO 'USERNAME' PRO SECURITY COOPERAR ;)
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll()
                        .loginProcessingUrl("/permit_login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", false)
                        .failureUrl("/login?error")
                )

                // LOGOUT VAI INVALIDAR A SESSÃO E MATAR OS COOKIES
                // A /API VAI ADICIONAR O TOKEN À UMA BLACKLIST
                .logout(logout -> logout
                        .logoutUrl("/logout-web")
                        .addLogoutHandler(cookieLogoutHandler)
                        .logoutSuccessUrl("/?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
