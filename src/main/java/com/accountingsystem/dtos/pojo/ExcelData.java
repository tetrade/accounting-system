package com.accountingsystem.dtos.pojo;

import com.accountingsystem.dtos.ContractDto;
import com.accountingsystem.dtos.CounterpartyContractDto;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class ExcelData {
    Set<ContractDto> contracts;
    Set<CounterpartyContractDto> counterpartyContracts;
}
