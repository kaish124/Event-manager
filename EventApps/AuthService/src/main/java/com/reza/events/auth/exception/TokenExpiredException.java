package com.reza.events.auth.exception;

public class TokenExpiredException extends AuthException {

    public static final String TOKEN_EXPIRED = "token.expired";

    public TokenExpiredException() {
        super("JWT token has expired. Please log in again.", TOKEN_EXPIRED);
    }

    public TokenExpiredException(Throwable cause) {
        super("JWT token has expired. Please log in again.", TOKEN_EXPIRED, cause);
    }
}
