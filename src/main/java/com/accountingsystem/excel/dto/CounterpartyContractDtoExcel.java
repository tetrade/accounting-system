package com.accountingsystem.excel.dto;


import com.accountingsystem.excel.enums.EContractType;
import lombok.Data;
import lombok.EqualsAndHashCode;



// для избежания нарушения принципа LSP создадим новый класс
@EqualsAndHashCode(callSuper = true)
@Data
public class CounterpartyContractDtoExcel extends ExcelContractTemplate {
    private ContractDtoExcel contractDtoExcel;


    @Override
    public String getInnerContractName() {
        if (contractDtoExcel != null) return contractDtoExcel.getName();
        return "";
    }

    @Override
    public String getContactType() {
        return EContractType.COUNTERPARTY_CONTRACT.getType();
    }
}
