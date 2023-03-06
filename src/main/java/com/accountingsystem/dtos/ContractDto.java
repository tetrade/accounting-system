package com.accountingsystem.dtos;


import com.accountingsystem.enums.EType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
public class ContractDto {
    private int id;
    private String name;
    private EType type;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private BigDecimal amount;
    private Set<CounterpartyContractDto> counterpartyContracts;
    private Set<ContractStageDto> contractStages;
}
