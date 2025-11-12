package com.nebulohub.infra.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

// ESSA CLASSE SERVE PRA DIFERENCIAR 'ACCESS DENIED'S DE BROWSER VS. JSON.
// JSON VAI RECEBER 403; WEB VAI SER REDIRECIONADA PARA ACCESS-DENIED
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        logger.debug("Access denied for {}: {}", request.getRequestURI(), accessDeniedException.getMessage());

        String accept = request.getHeader("Accept");
        boolean htmlPreferred = accept != null && accept.contains("text/html");

        if (htmlPreferred) {
            String redirectUrl = request.getContextPath() + "/access-denied";
            response.sendRedirect(redirectUrl);
            return;
        }

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}
