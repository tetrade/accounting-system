package com.accountingsystem.controller.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private int id;
    private String login;
    private String fullName;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate terminationDate;

    private Boolean isAdmin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // TODO Возможность редактирования пользователя
}
