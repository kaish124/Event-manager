package com.reza.events.auth.exception;

public class TokenInvalidException extends AuthException {

    public static final String TOKEN_INVALID = "token.invalid";

    public TokenInvalidException(){
        super("Invalid JWT token", TOKEN_INVALID);
    }

    public TokenInvalidException(Throwable cause){
        super("Invalid JWT token", TOKEN_INVALID, cause);
    }
}
