package com.accountingsystem.dtos;

import com.accountingsystem.entitys.enums.EType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
abstract class AbstractContract {
    private String name;
    private EType type;
    private BigDecimal amount;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
}
