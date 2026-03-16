package com.reza.events.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class FieldViolation {

    private final String field;
    private final String errorCode;
    private final String message;
    private final Object rejectedValue;
}
