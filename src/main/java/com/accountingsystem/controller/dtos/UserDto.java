package com.accountingsystem.controller.dtos;

import com.accountingsystem.entitys.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserDto {
    private int id;
    private String login;
    private String fullName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
