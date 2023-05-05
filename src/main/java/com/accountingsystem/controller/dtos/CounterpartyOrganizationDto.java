package com.accountingsystem.controller.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CounterpartyOrganizationDto {
    public int id;

    @NotBlank
    public String name;

    @NotBlank
    public String address;

    @Pattern(regexp = "(^\\d{12}$)|(^\\d{10}$)", message = "инн должен содержать либо 12, либо 10 символов")
    @NotBlank
    public String inn;
}
