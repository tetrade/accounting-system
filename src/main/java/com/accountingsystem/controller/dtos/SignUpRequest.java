package com.accountingsystem.controller.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {

    @Size(min = 6, message = "не должно быть меньше 6 символов")
    @NotBlank
    private String login;


    @Pattern(regexp = "^.*(?=.{6,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#$%&? \"]).*$",
            message = "должно быть не меньше 6 символов и должно содержать не менее одной буквы латиницы, цифры и спец-символа")
    @NotBlank
    private String password;

    @NotBlank
    private String fullName;
}
