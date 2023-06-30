package com.accountingsystem.excel.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;



// для избежания нарушения принципа LSP создадим новый класс
@EqualsAndHashCode(callSuper = true)
@Data
public class CounterpartyContractDtoExcel extends AbstractExcelContract {
    private ContractDtoExcel contractDtoExcel;
}
