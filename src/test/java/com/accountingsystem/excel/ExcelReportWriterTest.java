package com.accountingsystem.excel;

import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

// Тесты проверяют не стили ячеек, а их наполнение и порядок следования !
class ExcelReportWriterTest {

    public ExcelReportWriter excelReportWriter = new ExcelReportWriter();

    @Test
    void shouldCreateRightContractStageReport_whenCallCreateContractStagesReport() throws IOException, SAXException {

        ContractStageDtoExcel contractStageDtoExcel = new ContractStageDtoExcel();
        contractStageDtoExcel.setName("Первый контракт");
        contractStageDtoExcel.setAmount(196.32);
        contractStageDtoExcel.setActualEndDate(DateUtil.getExcelDate(LocalDate.of(2001, 7, 16)));
        contractStageDtoExcel.setActualStartDate(DateUtil.getExcelDate(LocalDate.of(2008, 9, 15)));
        contractStageDtoExcel.setPlannedEndDate(DateUtil.getExcelDate(LocalDate.of(2002, 8, 17)));
        contractStageDtoExcel.setPlannedStartDate(DateUtil.getExcelDate(LocalDate.of(2003, 9, 18)));
        contractStageDtoExcel.setActualMaterialCosts(38314.12);
        contractStageDtoExcel.setPlannedMaterialCosts(901760.95);
        contractStageDtoExcel.setPlannedSalaryExpenses(555555.94);
        contractStageDtoExcel.setActualSalaryExpenses(9014876.58);

        ContractStageDtoExcel contractStageDtoExcel1 = new ContractStageDtoExcel();
        contractStageDtoExcel1.setName("Второй контракт");
        contractStageDtoExcel1.setAmount(20000.32);
        contractStageDtoExcel1.setActualStartDate(DateUtil.getExcelDate(LocalDate.of(2008, 9, 15)));
        contractStageDtoExcel1.setActualEndDate(DateUtil.getExcelDate(LocalDate.of(2009, 7, 10)));
        contractStageDtoExcel1.setPlannedEndDate(DateUtil.getExcelDate(LocalDate.of(2010, 11, 20)));
        contractStageDtoExcel1.setPlannedStartDate(DateUtil.getExcelDate(LocalDate.of(2011, 3, 15)));
        contractStageDtoExcel1.setActualMaterialCosts(190100.74);
        contractStageDtoExcel1.setPlannedMaterialCosts(100001.95);
        contractStageDtoExcel1.setPlannedSalaryExpenses(555555.94);
        contractStageDtoExcel1.setActualSalaryExpenses(8888481.58);


        Set<ContractStageDtoExcel> dataForTest =
                Stream.of(contractStageDtoExcel, contractStageDtoExcel1).collect(Collectors.toSet());
        XSSFWorkbook workbook = new XSSFWorkbook(excelReportWriter.createContractStagesReport(dataForTest).toInputStream());

        XSSFSheet expectedSheet = workbook.getSheetAt(0);

        String[][] expectedContent = {
                {"Основная таблица", "", "", "", "", ""},
                {
                    "№", "Название", "Сумма", "Плановая дата начала", "Плановая дата окончания", "Фактическая дата начала",
                        "Фактическая дата окончания", "Плановые расходы на материалы", "Фактические расходы на материалы",
                        "Плановые расходы на зарплату", "Фактические расходы на зарплату"
                },
                {
                    "1.0", "Первый контракт", "196.32", "18-сен-2003", "17-авг-2002", "15-сен-2008", "16-июл-2001",
                        "901760.95", "38314.12", "555555.94", "9014876.58"
                },
                {
                    "2.0", "Второй контракт", "20000.32", "15-мар-2011", "20-ноя-2010", "15-сен-2008", "10-июл-2009",
                        "100001.95", "190100.74", "555555.94", "8888481.58"
                }
        };

        for (int j = 0; j <= expectedSheet.getLastRowNum(); j++) {
            Row row = expectedSheet.getRow(j);
            for (int k = 0; k < row.getLastCellNum(); k++) {
                assertThat(expectedContent[j][k]).isEqualToIgnoringWhitespace(row.getCell(k).toString());
            }
        }
    }


    @Test
    void shouldReturnContractReport_whenCreateContractReport() throws IOException {
        ContractDtoExcel contract = new ContractDtoExcel();
        contract.setName("Test contract 1");
        contract.setAmount(8891.01);
        contract.setActualStartDate(DateUtil.getExcelDate(LocalDate.of(2021, 5, 21)));
        contract.setPlannedStartDate(DateUtil.getExcelDate(LocalDate.of(2021, 8, 1)));
        contract.setActualEndDate(DateUtil.getExcelDate(LocalDate.of(2021, 7, 8)));
        contract.setPlannedEndDate(DateUtil.getExcelDate(LocalDate.of(2021, 9, 20)));

        ContractDtoExcel contract1 = new ContractDtoExcel();
        contract1.setName("Test contract 2");
        contract1.setAmount(58719.23);
        contract1.setActualStartDate(DateUtil.getExcelDate(LocalDate.of(2025, 5, 21)));
        contract1.setPlannedStartDate(DateUtil.getExcelDate(LocalDate.of(2021, 8, 1)));
        contract1.setActualEndDate(DateUtil.getExcelDate(LocalDate.of(2021, 3, 25)));
        contract1.setPlannedEndDate(DateUtil.getExcelDate(LocalDate.of(2021, 11, 11)));

        ContractDtoExcel contract2 = new ContractDtoExcel();
        contract2.setName("Test contract 3");
        contract2.setAmount(45678.89);
        contract2.setType(EType.PURCHASE.getType());
        contract2.setActualStartDate(DateUtil.getExcelDate(LocalDate.of(2007, 5, 21)));
        contract2.setPlannedStartDate(DateUtil.getExcelDate(LocalDate.of(2008, 8, 1)));
        contract2.setActualEndDate(DateUtil.getExcelDate(LocalDate.of(2010, 9, 9)));
        contract2.setPlannedEndDate(DateUtil.getExcelDate(LocalDate.of(2010, 9, 9)));

        CounterpartyContractDtoExcel counterpartyContract = new CounterpartyContractDtoExcel();
        counterpartyContract.setName("Test counterparty contract 1");
        counterpartyContract.setAmount(93.67);
        counterpartyContract.setType(EType.DELIVERY.getType());
        counterpartyContract.setPlannedStartDate(DateUtil.getExcelDate(LocalDate.of(2021, 1, 1)));
        counterpartyContract.setActualStartDate(DateUtil.getExcelDate(LocalDate.of(2021, 2, 2)));
        counterpartyContract.setActualEndDate(DateUtil.getExcelDate(LocalDate.of(2022, 3, 3)));
        counterpartyContract.setContractDtoExcel(contract);

        CounterpartyContractDtoExcel counterpartyContract1 = new CounterpartyContractDtoExcel();
        counterpartyContract1.setName("Test counterparty contract 2");
        counterpartyContract1.setAmount(93.67);
        counterpartyContract1.setType(EType.WORKS.getType());
        counterpartyContract1.setPlannedStartDate(DateUtil.getExcelDate(LocalDate.of(2021, 1, 1)));
        counterpartyContract1.setActualStartDate(DateUtil.getExcelDate(LocalDate.of(2021, 2, 2)));
        counterpartyContract1.setActualEndDate(DateUtil.getExcelDate(LocalDate.of(2022, 3, 3)));
        counterpartyContract1.setContractDtoExcel(contract);

        CounterpartyContractDtoExcel counterpartyContract2 = new CounterpartyContractDtoExcel();
        counterpartyContract2.setName("Test counterparty contract 3");
        counterpartyContract2.setAmount(93.67);
        counterpartyContract2.setType(EType.DELIVERY.getType());
        counterpartyContract2.setPlannedStartDate(DateUtil.getExcelDate(LocalDate.of(2021, 1, 1)));
        counterpartyContract2.setActualStartDate(DateUtil.getExcelDate(LocalDate.of(2021, 2, 2)));
        counterpartyContract2.setActualEndDate(DateUtil.getExcelDate(LocalDate.of(2022, 3, 3)));
        counterpartyContract2.setContractDtoExcel(contract1);

        Set<ContractDtoExcel> contractSet = Stream.of(contract, contract2).collect(Collectors.toCollection(LinkedHashSet::new));
        Set<CounterpartyContractDtoExcel> counterpartyContractDtoExcelSet =
                Stream.of(counterpartyContract, counterpartyContract1, counterpartyContract2).collect(Collectors.toCollection(LinkedHashSet::new));

        XSSFWorkbook workbook =
                new XSSFWorkbook(excelReportWriter.createContractReport(counterpartyContractDtoExcelSet, contractSet)
                        .toInputStream());

        XSSFSheet expectedSheet = workbook.getSheetAt(0);

        String[][] expectedContent = {
                {
                    "Все договоры за период", "", "", "", "", ""
                },
                {
                        "№", "Тип договора", "Название", "Цель договора", "Сумма", "Плановая дата начала", "Плановая дата окончания",
                        "Фактическая дата начала", "Фактическая дата окончания", "Основной контракт"
                },
                {
                        "1.0", "Договор с контрагентом", "Test counterparty contract 1", "Поставка", "93.67", "01-янв-2021", "",
                        "02-фев-2021", "03-мар-2022", "Test contract 1"
                },
                {
                        "2.0", "Договор с контрагентом", "Test counterparty contract 2", "Работы", "93.67", "01-янв-2021",
                        "", "02-фев-2021", "03-мар-2022", "Test contract 1"
                },
                {
                    "3.0", "Договор с контрагентом", "Test counterparty contract 3", "Поставка", "93.67", "01-янв-2021",
                        "", "02-фев-2021", "03-мар-2022", "Test contract 2"
                },
                {
                    "4.0", "Договор", "Test contract 1", "", "8891.01", "01-авг-2021", "20-сен-2021", "21-мая-2021",
                        "08-июл-2021"
                },
                {
                    "5.0", "Договор", "Test contract 3", "Закупка", "45678.89", "01-авг-2008", "09-сен-2010",
                        "21-мая-2007", "09-сен-2010"
                },
                {""}, {""},
                {
                        "Связанные контракты", "", "", "", "", ""
                }
                ,
                {
                    "№", "Тип договора", "Название", "Цель договора", "Сумма", "Плановая дата начала", "Плановая дата окончания",
                        "Фактическая дата начала", "Фактическая дата окончания"
                },
                {
                    "1.0", "Договор", "Test contract 1", "", "8891.01", "01-авг-2021", "20-сен-2021", "21-мая-2021",
                        "08-июл-2021"
                },
                {
                    "2.0", "Договор", "Test contract 2", "", "58719.23", "01-авг-2021", "11-ноя-2021", "21-мая-2025",
                        "25-мар-2021"
                }
        };

        for (int j = 0; j <= expectedSheet.getLastRowNum(); j++) {
            Row row = expectedSheet.getRow(j);
            if (row != null) {
                for (int k = 0; k < row.getLastCellNum(); k++) {
                    assertThat(expectedContent[j][k]).isEqualTo(row.getCell(k).toString());
                }
            }
        }

    }
}
