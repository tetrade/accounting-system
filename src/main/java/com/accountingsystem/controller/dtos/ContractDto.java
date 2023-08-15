package com.accountingsystem.controller.dtos;


import com.accountingsystem.controller.customValidator.EndDateAfterStart;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@JsonPropertyOrder({ "id", "name" })
@Validated
public class ContractDto extends AbstractContract{
    private int id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<@Valid ContractStageDto> contractStages;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<@Valid CounterpartyContractDto> counterpartyContracts;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Positive
    private Integer userId;
}
