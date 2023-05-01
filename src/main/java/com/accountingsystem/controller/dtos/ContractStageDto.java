package com.accountingsystem.controller.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContractStageDto {
    private int id;
    private String name;
    private BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate actualStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate actualEndDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate plannedStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate plannedEndDate;

    private BigDecimal actualMaterialCosts;
    private BigDecimal plannedMaterialCosts;
    private BigDecimal actualSalaryExpenses;
    private BigDecimal plannedSalaryExpenses;
}
