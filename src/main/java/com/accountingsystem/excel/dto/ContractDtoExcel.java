package com.accountingsystem.excel.dto;

import com.accountingsystem.excel.enums.EContractType;

public class ContractDtoExcel extends ExcelContractTemplate {
    @Override
    public String getInnerContractName() {
        return "";
    }

    @Override
    public String getContactType() {
        return EContractType.MAIN.getType();
    }
}
