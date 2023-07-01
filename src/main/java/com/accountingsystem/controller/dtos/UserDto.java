package com.accountingsystem.controller.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UserDto {
    private int id;

    @Size(min = 6, message = "не должно быть меньше 6 символов")
    @NotBlank
    private String login;

    @NotBlank
    private String fullName;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @NotNull
    private LocalDate terminationDate;

    @NotNull
    private Boolean isAdmin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^.*(?=.{6,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#$%&? \"]).*$",
            message = "должно быть не меньше 6 символов и должно содержать не менее одной буквы латиницы, цифры и спец-символа")
    private String newPassword;
}
