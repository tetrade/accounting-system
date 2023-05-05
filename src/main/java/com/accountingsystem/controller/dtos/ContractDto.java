package com.accountingsystem.controller.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.validation.constraints.Positive;


@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@JsonPropertyOrder({ "id", "name" })
public class ContractDto extends AbstractContract{
    private int id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Positive
    private Integer userId;
}
