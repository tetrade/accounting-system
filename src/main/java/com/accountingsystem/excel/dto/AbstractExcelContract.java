package com.accountingsystem.excel.dto;

import lombok.Data;

@Data
public abstract class AbstractExcelContract {
    private String name;
    private String type;
    private Double amount;
    private Double plannedStartDate;
    private Double plannedEndDate;
    private Double actualStartDate;
    private Double actualEndDate;
}
