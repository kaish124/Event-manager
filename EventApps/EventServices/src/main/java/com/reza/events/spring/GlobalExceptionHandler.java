package com.reza.events.spring;

import com.reza.events.exception.*;
import com.reza.events.logging.LogKeys;
import com.reza.events.logging.Logger;
import com.reza.events.logging.LoggerFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldViolation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldViolation)
                .collect(Collectors.toList());

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "validation.failed",
                "Request validation failed.",
                extractPath(request),
                violations
        );

        LOGGER.debug(LogKeys.MSG, "Validation failed", "VIOLATIONS_COUNT", violations.size());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
        LOGGER.debug(LogKeys.MSG, "Validation failed", LogKeys.ERROR_MESSAGE, ex.getMessage());

        ErrorResponse body = ex.getFieldViolations().isEmpty()
                ? ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                ex.getErrorCode(),
                ex.getMessage(),
                extractPath(request))
                : ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                ex.getErrorCode(),
                ex.getMessage(),
                extractPath(request),
                ex.getFieldViolations());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        LOGGER.debug(LogKeys.MSG, "Resource not found", LogKeys.ERROR_MESSAGE, ex.getMessage());

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                ex.getErrorCode(),
                ex.getMessage(),
                extractPath(request)
        );

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex, WebRequest request) {
        LOGGER.error(ex, LogKeys.MSG, "Application exception", LogKeys.STATUS, ex.getStatus().value());

        ErrorResponse body = ErrorResponse.of(
                ex.getStatus().value(),
                ex.getErrorCode(),
                ex.getMessage(),
                extractPath(request)
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<FieldViolation> violations = ex.getConstraintViolations()
                .stream()
                .map(this::toFieldViolation)
                .collect(Collectors.toList());

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "constraint.violation",
                "Constraint violation.",
                extractPath(request),
                violations
        );

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailure(OptimisticLockingFailureException ex, WebRequest request) {
        LOGGER.warn(LogKeys.MSG, "Optimistic locking failure", LogKeys.ERROR_MESSAGE, ex.getMessage());

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                "concurrent.modification",
                "The resource was modified by another request. Please reload and try again.",
                extractPath(request)
        );

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, WebRequest request) {
        // Don't handle AuthenticationException - let Spring Security handle it
        if (ex instanceof org.springframework.security.core.AuthenticationException) {
            throw (org.springframework.security.core.AuthenticationException) ex;
        }
        
        // Don't handle AccessDeniedException - let Spring Security handle it
        if (ex instanceof org.springframework.security.access.AccessDeniedException) {
            throw (org.springframework.security.access.AccessDeniedException) ex;
        }
        
        // Don't handle AuthorizationDeniedException - let Spring Security handle it
        if (ex instanceof org.springframework.security.authorization.AuthorizationDeniedException) {
            throw (org.springframework.security.authorization.AuthorizationDeniedException) ex;
        }
        
        LOGGER.error(ex, LogKeys.MSG, "Unhandled exception", LogKeys.PATH, extractPath(request));

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "internal.error",
                "An unexpected error occurred. Please try again later.",
                extractPath(request)
        );

        return ResponseEntity.badRequest().body(body);
    }


    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    private FieldViolation toFieldViolation(FieldError fieldError) {
        return new FieldViolation(
                fieldError.getField(),
                fieldError.getCode(),
                fieldError.getDefaultMessage(),
                fieldError.getRejectedValue()
        );
    }
    private FieldViolation toFieldViolation(ConstraintViolation<?> cv) {
        return new FieldViolation(
                cv.getPropertyPath().toString(),
                cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
                cv.getMessage(),
                String.valueOf(cv.getInvalidValue())
        );
    }
}