package com.auth.example.controllers;

import com.auth.example.controllers.dtos.ResponseDto;
import com.auth.example.exceptions.ApiPermissionDeniedDataAccessException;
import com.auth.example.exceptions.RequestBindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.PrematureJwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@ControllerAdvice
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorController extends DefaultHandlerExceptionResolver {

    private final ObjectMapper objectMapper;

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<?> unresolvedError(Exception e){
        e.printStackTrace();
        return ResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR,
                ResponseDto.ErrorMessages.builder().message("something.went.wrong").build());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<?> unsupportedOps(UnsupportedOperationException e){
        e.printStackTrace();
        return ResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR,
                ResponseDto.ErrorMessages.builder().message("unsupported.operation").build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDenied(AccessDeniedException e){
        e.printStackTrace();
        return ResponseDto.error(HttpStatus.FORBIDDEN,
                ResponseDto.ErrorMessages.builder().message("access.denied").build());
    }

    @ExceptionHandler(RequestBindException.class)
    public ResponseEntity<?> requestBindingError(RequestBindException requestBindException){
        requestBindException.printStackTrace();
        return ResponseDto.badRequest(requestBindException.getErrorMessages()
                .toArray(new ResponseDto.ErrorMessages[]{}));
    }

    @ExceptionHandler(PrematureJwtException.class)
    public ResponseEntity<?> prematureJwtException(){
        return ResponseDto.badRequest(ResponseDto.ErrorMessages.builder().message("premature.jwt.token").build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        ResponseDto.ErrorMessages[] errorMessages = methodArgumentNotValidException.getFieldErrors()
                .stream().map(fieldError -> ResponseDto.ErrorMessages.builder().message(fieldError.getDefaultMessage())
                        .args(fieldError.getArguments()).build())
                .collect(Collectors.toSet()).toArray(new ResponseDto.ErrorMessages[]{});
        return ResponseDto.badRequest(errorMessages);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> jwtException(){
        return ResponseDto.badRequest(ResponseDto.ErrorMessages.builder().message("invalid.token.supplied").build());
    }

    @ExceptionHandler(ApiPermissionDeniedDataAccessException.class)
    public ResponseEntity<?> apiPermissionDenied(ApiPermissionDeniedDataAccessException ex){
        return ResponseDto.error(HttpStatus.FORBIDDEN,
                ResponseDto.ErrorMessages.builder().message(ex.getMessage()).build());
    }

    @SneakyThrows
    public void resolvedFilterError(HttpServletResponse httpServletResponse, String message) {
        ResponseDto.ErrorResponseDto errorResponseDto = new ResponseDto.ErrorResponseDto();
        errorResponseDto.setSuccess(false);
        errorResponseDto.setMessages(new String[]{ResponseDto.getMessage(message)});
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
    }
}
