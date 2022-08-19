package com.auth.example.controllers.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginDto {
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
}
