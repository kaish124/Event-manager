package com.reza.events.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException{

    private final HttpStatus status;
    private final String errorCode;

    public AppException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public AppException(String message, String errorCode, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }
}
