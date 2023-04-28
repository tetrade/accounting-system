package com.accountingsystem.controller.dtos;

import com.accountingsystem.entitys.enums.ERole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserDto {
    private int id;
    private String login;
    private String fullName;
    private LocalDate terminationDate;

    private Set<ERole> roles;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // TODO Возможность редактирования пользователя
}
