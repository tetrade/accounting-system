package com.accountingsystem.dtos;

import com.accountingsystem.enums.EType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CounterpartyContractDto {
    private int id;
    private String name;
    private EType type;
    private CounterpartyOrganizationDto counterpartyOrganization;
    private BigDecimal amount;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
}
