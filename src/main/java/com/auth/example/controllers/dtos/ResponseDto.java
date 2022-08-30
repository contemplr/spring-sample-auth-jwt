package com.auth.example.controllers.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

@Getter
@Setter
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private String message;

    public static <T> ResponseEntity<ResponseDto<T>> success(T data, String message, Object... args) {
        ResponseDto<T> responseDto = new ResponseDto<>();
        responseDto.setData(data);
        responseDto.setSuccess(true);
        responseDto.setMessage(getMessage(message, args));
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ErrorResponseDto> badRequest(ErrorMessages... errorMessages) {
        return error(HttpStatus.BAD_REQUEST, errorMessages);
    }

    public static ResponseEntity<ErrorResponseDto> error(HttpStatus status,
                                                         ErrorMessages... errorMessages) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setSuccess(false);
        errorResponseDto.setMessages(getMessages(errorMessages));
        return ResponseEntity.status(status).body(errorResponseDto);
    }

    @Getter
    @Setter
    public static class ErrorResponseDto {
        private boolean success;
        private String[] messages;
    }

    @Getter
    @Setter
    @Builder
    public static class ErrorMessages {
        private String message;
        private Object[] args;
    }

    public static ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource =
                new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename("classpath:messages");
        reloadableResourceBundleMessageSource.setUseCodeAsDefaultMessage(true);
        reloadableResourceBundleMessageSource.setDefaultLocale(Locale.ENGLISH);
        return reloadableResourceBundleMessageSource;
    }

    public static String getMessage(String message, Object... args){
        MessageSource messageSource = messageSource();
        if(args != null) {
            Object[] objects = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                objects[i] = messageSource.getMessage(String.valueOf(args[i]), null, Locale.ENGLISH);
            }
            return messageSource.getMessage(message, objects, Locale.ENGLISH);
        }
        return messageSource.getMessage(message, null, Locale.ENGLISH);
    }

    public static String[] getMessages(ErrorMessages... errorMessages){
        String[] messages = new String[errorMessages.length];

        for (int i = 0; i < errorMessages.length; i++) {
            messages[i] = getMessage(errorMessages[i].message, errorMessages[i].args);
        }
        return messages;
    }
}
