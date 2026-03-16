package com.reza.events.exception;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Builder
@Getter
public final class ErrorResponse implements Serializable {

    private final int status;
    private final String errorCode;
    private final String message;
    private final Instant timestamp;
    private final String path;
    private final List<FieldViolation> fieldViolations;

    public static ErrorResponse of(int status, String errorCode, String message, String path){
        return ErrorResponse.builder()
                .status(status)
                .errorCode(errorCode)
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .build();
    }

    public static ErrorResponse of(int status, String errorCode, String message, String path, List<FieldViolation> fieldViolations){
        return ErrorResponse.builder()
                .status(status)
                .errorCode(errorCode)
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .fieldViolations(fieldViolations)
                .build();
    }
 }
