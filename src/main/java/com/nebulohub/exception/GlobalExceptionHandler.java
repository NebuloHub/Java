package com.nebulohub.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // VERIFICA SE A REQUEST PRECISA DE HTML
    private boolean wantsHtml(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        if (accept == null) return false;
        return accept.contains("text/html");
    }

    // BUILDER PARA ErrorResponse JSON (MODIFIED: Added path)
    private ResponseEntity<ErrorResponse> buildJson(HttpStatus status, String error, String message, String path) {
        ErrorResponse err = new ErrorResponse(status.value(), error, message, path);
        return new ResponseEntity<>(err, status);
    }

    // ========== NotFoundException ==========
    @ExceptionHandler(NotFoundException.class)
    public Object handleNotFound(NotFoundException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (wantsHtml(request)) {
            ModelAndView mav = new ModelAndView("error/404");
            mav.addObject("status", HttpStatus.NOT_FOUND.value());
            mav.addObject("error", "Resource not found");
            mav.addObject("message", ex.getMessage());
            mav.addObject("path", path);
            return mav;
        }
        return buildJson(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage(), path);
    }

    // ========== Validation ERRORS ==========
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String message = errors.toString();
        String path = request.getRequestURI();

        if (wantsHtml(request)) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("status", HttpStatus.BAD_REQUEST.value());
            mav.addObject("error", "Validation failed");
            mav.addObject("message", message);
            mav.addObject("path", path);
            return mav;
        }
        return buildJson(HttpStatus.BAD_REQUEST, "Validation failed", message, path);
    }

    // ========== DuplicateEntry ==========
    @ExceptionHandler(DuplicateEntryException.class)
    public Object handleDuplicateEntry(DuplicateEntryException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (wantsHtml(request)) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("status", HttpStatus.CONFLICT.value());
            mav.addObject("error", "Duplicate entry");
            mav.addObject("message", ex.getMessage());
            mav.addObject("path", path);
            return mav;
        }
        return buildJson(HttpStatus.CONFLICT, "Duplicate entry", ex.getMessage(), path);
    }

    // ========== AccessDenied ==========
    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (wantsHtml(request)) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("status", HttpStatus.FORBIDDEN.value());
            mav.addObject("error", "Access denied");
            mav.addObject("message", ex.getMessage());
            mav.addObject("path", path);
            return mav;
        }
        return buildJson(HttpStatus.FORBIDDEN, "Access denied", ex.getMessage(), path);
    }

    // ========== IllegalArgument ==========
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (wantsHtml(request)) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("status", HttpStatus.BAD_REQUEST.value());
            mav.addObject("error", "Invalid argument");
            mav.addObject("message", ex.getMessage());
            mav.addObject("path", path);
            return mav;
        }
        return buildJson(HttpStatus.BAD_REQUEST, "Invalid argument", ex.getMessage(), path);
    }

    // ========== BusinessException ========== (Added this handler)
    @ExceptionHandler(BusinessException.class)
    public Object handleBusinessException(BusinessException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (wantsHtml(request)) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("status", HttpStatus.BAD_REQUEST.value());
            mav.addObject("error", "Business Rule Violation");
            mav.addObject("message", ex.getMessage());
            mav.addObject("path", path);
            return mav;
        }
        // Using 400 Bad Request for business logic failures (like self-rating)
        return buildJson(HttpStatus.BAD_REQUEST, "Business Rule Violation", ex.getMessage(), path);
    }

    // ========== DataIntegrityViolation ==========
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (wantsHtml(request)) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("status", HttpStatus.CONFLICT.value());
            mav.addObject("error", "Data integrity violation");
            mav.addObject("message", "Operation would violate data integrity constraints");
            mav.addObject("path", path);
            return mav;
        }
        return buildJson(HttpStatus.CONFLICT, "Data integrity violation", "Operation would violate data integrity constraints", path);
    }

    // ========== Authentication exceptions ==========
    @ExceptionHandler(AuthenticationException.class)
    public Object handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (wantsHtml(request)) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("status", HttpStatus.UNAUTHORIZED.value());
            mav.addObject("error", "Authentication Failed");
            mav.addObject("message", ex.getMessage());
            mav.addObject("path", path);
            return mav;
        }
        return buildJson(HttpStatus.UNAUTHORIZED, "Authentication Failed", ex.getMessage(), path);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Object handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (wantsHtml(request)) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("status", HttpStatus.UNAUTHORIZED.value());
            mav.addObject("error", "Authentication failed");
            mav.addObject("message", "Login ou Senha inválidos.");
            mav.addObject("path", path);
            return mav;
        }
        return buildJson(HttpStatus.UNAUTHORIZED, "Authentication failed", "Login ou Senha Inválida", path);
    }

    @ExceptionHandler(DisabledException.class)
    public Object handleDisabledUser(DisabledException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (wantsHtml(request)) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("status", HttpStatus.UNAUTHORIZED.value());
            mav.addObject("error", "Authentication failed");
            mav.addObject("message", "Conta Desativada");
            mav.addObject("path", path);
            return mav;
        }
        return buildJson(HttpStatus.UNAUTHORIZED, "Authentication failed", "Conta Desativada", path);
    }

    // ========== GENERIC ==========
    @ExceptionHandler(Exception.class)
    public Object handleGenericException(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (wantsHtml(request)) {
            ModelAndView mav = new ModelAndView("error/error");
            mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            mav.addObject("error", "Erro Inesperado Aconteceu");
            mav.addObject("message", ex.getMessage());
            mav.addObject("path", path);
            return mav;
        }
        return buildJson(HttpStatus.INTERNAL_SERVER_ERROR, "Erro Inesperado Aconteceu", ex.getMessage(), path);
    }
}