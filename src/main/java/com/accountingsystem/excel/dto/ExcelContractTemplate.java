package com.accountingsystem.excel.dto;

import lombok.Data;

// Реализуем шаблон проектирование Template
@Data
public abstract class ExcelContractTemplate {
    private String name;
    private String type;
    private Double amount;
    private Double plannedStartDate;
    private Double plannedEndDate;
    private Double actualStartDate;
    private Double actualEndDate;

    public abstract String getInnerContractName();
    public abstract String getContactType();
}
