package com.accountingsystem.controller.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {

    @Size(min = 5, message = "не должно быть меньше 5 символов")
    @NotBlank
    private String login;


    @Pattern(regexp = "^.*(?=.{6,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#$%&? \"]).*$",
            message = "должно быть не меньше 6 символов и должно содержать не менее одной буквы латиницы, цифры и спец-символа")
    @NotBlank
    private String password;

    @NotBlank
    @Pattern(regexp = "^([а-яА-Я]+ )([а-яА-Я]+ )([а-яА-Я]+)", message = "должен быть следующий формат:Ф И О")
    private String fullName;
}
