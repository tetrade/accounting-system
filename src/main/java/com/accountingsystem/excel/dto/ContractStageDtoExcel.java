package com.accountingsystem.excel.dto;

import lombok.Data;

@Data
public class ContractStageDtoExcel {
    private String name;
    private Double amount;
    private Double actualStartDate;
    private Double actualEndDate;
    private Double plannedStartDate;
    private Double plannedEndDate;
    private Double actualMaterialCosts;
    private Double plannedMaterialCosts;
    private Double actualSalaryExpenses;
    private Double plannedSalaryExpenses;
}
