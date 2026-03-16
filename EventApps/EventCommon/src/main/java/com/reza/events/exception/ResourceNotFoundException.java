package com.reza.events.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AppException{

    public static final String RESOURCE_NOT_FOUND = "resource.not.found";

    public ResourceNotFoundException(Class<?> clazz, Object id ) {
        super("%s with id %s not found.".formatted(clazz.getSimpleName(), id), toErrorCode(clazz), HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message){
        super(message, RESOURCE_NOT_FOUND,  HttpStatus.NOT_FOUND);
    }

    private static String toErrorCode(Class<?> clazz) {
        return camelToKebab(clazz);
    }
    private static String camelToKebab(Class<?> clazz) {
        return clazz.getSimpleName().replaceAll("([a-z])([A-Z])", "$1.$2").toLowerCase();
    }
}
