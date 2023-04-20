package com.accountingsystem.controller.dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class ContractUserDto {

    @JsonUnwrapped
    private ContractDto contract;

    private UserDto user;
}
