package com.accountingsystem.controller.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContractStageDto {
    private int id;

    @NotBlank
    private String name;

    @DecimalMin(value = "0.0")
    private BigDecimal amount;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate actualStartDate;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate actualEndDate;

    @NotNull
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate plannedStartDate;

    @NotNull
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate plannedEndDate;

    @DecimalMin(value = "0.0")
    private BigDecimal actualMaterialCosts;

    @DecimalMin(value = "0.0")
    private BigDecimal plannedMaterialCosts;

    @DecimalMin(value = "0.0")
    private BigDecimal actualSalaryExpenses;

    @DecimalMin(value = "0.0")
    private BigDecimal plannedSalaryExpenses;
}
