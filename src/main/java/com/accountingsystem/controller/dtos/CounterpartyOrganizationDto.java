package com.accountingsystem.controller.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CounterpartyOrganizationDto {
    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @Pattern(regexp = "(^\\d{12}$)|(^\\d{10}$)", message = "инн должен содержать либо 12, либо 10 символов")
    @NotBlank
    private String inn;
}
