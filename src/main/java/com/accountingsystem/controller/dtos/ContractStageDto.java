package com.accountingsystem.controller.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContractStageDto {
    private int id;
    private String name;
    private BigDecimal amount;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private BigDecimal actualMaterialCosts;
    private BigDecimal plannedMaterialCosts;
    private BigDecimal actualSalaryExpenses;
    private BigDecimal plannedSalaryExpenses;
}
