package com.accountingsystem.dtos;

import com.accountingsystem.entitys.Role;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserDto {
    private int id;
    private String login;
    private String fullName;
    private String password;
    private Set<Role> roles;
    private Set<ContractDto> contracts;
    private LocalDate dateOfTermination;
}
