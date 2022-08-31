package com.auth.example.exceptions;

import com.auth.example.controllers.dtos.ResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
public class RequestBindException extends Exception {

    private ArrayList<ResponseDto.ErrorMessages> errorMessages;

    public RequestBindException() {
        super();
        this.errorMessages = new ArrayList<>();
    }

    public RequestBindException(ResponseDto.ErrorMessages... errorMessages) {
        super();
        this.errorMessages = new ArrayList<>();
        this.errorMessages.addAll(Arrays.asList(errorMessages));
    }

    public RequestBindException(String message, Object... args) {
        super();
        this.errorMessages = new ArrayList<>();
        this.errorMessages.add(ResponseDto.ErrorMessages.builder().message(message).args(args).build());
    }

    public void addError(String message, Object... args) {
        this.errorMessages.add(ResponseDto.ErrorMessages.builder()
                .message(message).args(args).build());
    }

    public boolean hasErrors() {
        return !this.errorMessages.isEmpty();
    }
}
