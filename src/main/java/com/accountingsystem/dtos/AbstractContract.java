package com.accountingsystem.dtos;

import com.accountingsystem.entitys.enums.EType;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
abstract class AbstractContract {
    private String name;
    private EType type;
    private BigDecimal amount;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
}
