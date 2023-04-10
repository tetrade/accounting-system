package com.accountingsystem.dtos;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@JsonPropertyOrder({ "id", "name" })
public class ContractDto extends AbstractContract{
    private int id;
}
