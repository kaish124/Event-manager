package com.reza.events.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ValidationException extends AppException{

    private final List<FieldViolation> fieldViolations;
    public static final String VALIDATION_FAILED = "validation.failed";

    public ValidationException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.BAD_REQUEST);
        this.fieldViolations = List.of();
    }

    public ValidationException(List<FieldViolation> fieldViolations) {
        super("Validation failed: %d error(s).".formatted(fieldViolations.size()), VALIDATION_FAILED, HttpStatus.BAD_REQUEST);
        this.fieldViolations = List.copyOf(fieldViolations);
    }
}
