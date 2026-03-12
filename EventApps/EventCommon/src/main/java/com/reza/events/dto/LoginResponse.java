package com.reza.events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse{
    private String token;
    private long expires_in;
    private String tokenType;

    public static LoginResponse of(String token, long expires_in){
        return new LoginResponse(token, expires_in, "Bearer");
    }
}
