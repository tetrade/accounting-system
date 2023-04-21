package com.accountingsystem.controller.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@JsonPropertyOrder({ "id", "name" })
public class CounterpartyContractDto extends AbstractContract{
    private int id;
    private CounterpartyOrganizationDto counterpartyOrganization;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer counterpartyOrganizationId;
}