package com.accountingsystem.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@JsonPropertyOrder({ "id", "name" })
public class ContractDto extends AbstractContract{
    private int id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer userId;
}
