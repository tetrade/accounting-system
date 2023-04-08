package com.accountingsystem.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class CounterpartyContractDto extends AbstractContract{
    private int id;
    private CounterpartyOrganizationDto counterpartyOrganizationDto;
}
