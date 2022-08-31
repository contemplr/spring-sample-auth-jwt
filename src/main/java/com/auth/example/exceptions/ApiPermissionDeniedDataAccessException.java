package com.auth.example.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiPermissionDeniedDataAccessException extends RuntimeException {
    private String message;

    public ApiPermissionDeniedDataAccessException(String message){
        super();
        this.message = message;
    }
}
