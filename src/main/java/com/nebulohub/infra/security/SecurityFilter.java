package com.nebulohub.infra.security;

import com.nebulohub.domain.user.UserRepository;
import com.nebulohub.service.TokenBlacklistService;
import com.nebulohub.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        var token = recoverToken(request);

        if (token != null) {
            // Check if token is on the blacklist (from logout)
            if (tokenBlacklistService.isBlacklisted(token)) {
                // Reject blacklisted token
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been invalidated by logout.");
                return;
            }

            // Validate token
            var email = tokenService.validateToken(token);
            if (email != null) {
                // Valid Token, load user and set authentication context
                UserDetails user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new ServletException("User not found for token subject."));
                
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}