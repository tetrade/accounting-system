package com.accountingsystem.excel;

import com.accountingsystem.excel.dto.AbstractExcelContract;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import com.accountingsystem.excel.enums.EColumn;
import com.accountingsystem.excel.enums.EContractType;
import com.accountingsystem.excel.enums.EDataFormat;
import com.accountingsystem.excel.enums.EFont;
import lombok.NoArgsConstructor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@Component
public class ExcelReportWriter {

    private XSSFWorkbook workbook;
    private XSSFSheet mainSheet;

    public ByteArrayOutputStream createContractStagesReport(
            Set<ContractStageDtoExcel> contractStageDtoExcels
    ) throws IOException {
        workbook = new XSSFWorkbook();
        mainSheet = workbook.createSheet("Основной");

        this.createTableTitle("Основная таблица", mainSheet.createRow(0));

        List<EColumn> columns = Stream.of(
                EColumn.CURRENT_NUMBER,  EColumn.NAME, EColumn.AMOUNT, EColumn.PLANNED_START_DATE,
                EColumn.PLANNED_END_DATE, EColumn.ACTUAL_START_DATE, EColumn.ACTUAL_END_DATE,
                EColumn.PLANNED_MATERIAL_COSTS, EColumn.ACTUAL_MATERIAL_COSTS,
                EColumn.PLANNED_SALARY_EXPENSES, EColumn.ACTUAL_SALARY_EXPENSES
        ).collect(Collectors.toList());

        int startTableNum = mainSheet.getLastRowNum() + 1;

        Row row = mainSheet.createRow(startTableNum);
        for (int i = 0; i < columns.size(); i++) createColumnHeader(row, i, columns.get(i));

        int currentRow = 1;
        for(ContractStageDtoExcel cs: contractStageDtoExcels) {
            row = mainSheet.createRow(mainSheet.getLastRowNum() + 1);
            addContractStageToReport(cs, row, currentRow++);
        }

        createTableMarkup(0, columns.size() - 1,
                startTableNum, mainSheet.getLastRowNum(), true);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {
            FileOutputStream out = new FileOutputStream("contract_stage_demo.xlsx");
            workbook.write(stream);
            workbook.write(out);
            out.close();
        }
        catch (Exception e)  { e.printStackTrace(); }
        workbook.close();
        return stream;
    }

    public ByteArrayOutputStream createContractReport(
            Set<CounterpartyContractDtoExcel> counterpartyContractDtos, Set<ContractDtoExcel> contractDtos
            ) throws IOException {

        workbook = new XSSFWorkbook();
        mainSheet = workbook.createSheet("Основной лист");

        int rowsBetweenTables = 3;

        this.createTableTitle("Все договоры за период", mainSheet.createRow(0));

        List<EColumn> columns = Stream.of(
                EColumn.CURRENT_NUMBER, EColumn.TYPE_MAIN, EColumn.NAME, EColumn.TYPE, EColumn.AMOUNT, EColumn.PLANNED_START_DATE,
                EColumn.PLANNED_END_DATE, EColumn.ACTUAL_START_DATE, EColumn.ACTUAL_END_DATE, EColumn.RELATED_CONTRACT
        ).collect(Collectors.toList());

        int startTableNum = mainSheet.getLastRowNum() + 1;

        Row row = mainSheet.createRow(startTableNum);
        for (int i = 0; i < columns.size(); i++) createColumnHeader(row, i, columns.get(i));


        List<ContractDtoExcel> referencedContracts = new ArrayList<>();
        int currentRow = 1;
        for(CounterpartyContractDtoExcel cc: counterpartyContractDtos) {
                row = mainSheet.createRow(mainSheet.getLastRowNum() + 1);
                addContractToReport(cc, row, currentRow++);

                if (columns.contains(EColumn.RELATED_CONTRACT)) {
                    int index = referencedContracts.indexOf(cc.getContractDtoExcel());
                    if (index == -1) {
                        referencedContracts.add(cc.getContractDtoExcel());
                        index = referencedContracts.size() - 1;
                    }
                    index = index + contractDtos.size() + counterpartyContractDtos.size() + rowsBetweenTables + 3;
                    Hyperlink h = workbook.getCreationHelper().createHyperlink(HyperlinkType.DOCUMENT);
                    String sb = '\'' + mainSheet.getSheetName() + '\'' + "!A" + (index + 1);
                    h.setAddress(sb);
                    row.getCell(row.getLastCellNum() - 1).setHyperlink(h);
                }
        }

        for(ContractDtoExcel c: contractDtos) {
            row = mainSheet.createRow(mainSheet.getLastRowNum() + 1);
            addContractToReport(c, row, currentRow++);
        }

        createTableMarkup(0, columns.size() - 1,
                startTableNum, mainSheet.getLastRowNum(), true);

        if (!referencedContracts.isEmpty()) {
            columns.remove(EColumn.RELATED_CONTRACT);
            row = mainSheet.createRow(mainSheet.getLastRowNum() + rowsBetweenTables);

            this.createTableTitle("Связанные контракты", row);

            startTableNum = mainSheet.getLastRowNum() + 1;
            row = mainSheet.createRow(startTableNum);

            for (int i = 0; i < columns.size(); i++) createColumnHeader(row, i, columns.get(i));

            currentRow = 1;
            for (ContractDtoExcel c : referencedContracts) {
                row = mainSheet.createRow(mainSheet.getLastRowNum() + 1);
                addContractToReport(c, row, currentRow++);
            }

            createTableMarkup(0, columns.size() - 1,
                    startTableNum, startTableNum + referencedContracts.size(), false);
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {
            FileOutputStream out = new FileOutputStream("contract_demo.xlsx");
            workbook.write(stream);
            workbook.write(out);
            out.close();
        }
        catch (Exception e)  { e.printStackTrace(); }
        workbook.close();
        return stream;
    }

    private void addContractToReport(AbstractExcelContract contract, Row row, Integer rowNUm) {
        int currentCell = 0;
        createCell(row, currentCell++, rowNUm);
        if (contract instanceof CounterpartyContractDtoExcel) {
            createCell(row, currentCell++, EContractType.COUNTERPARTY_CONTRACT.getType());
        } else {
            createCell(row, currentCell++, EContractType.MAIN.getType());
        }
        createCell(row, currentCell++, contract.getName());
        createCell(row, currentCell++, contract.getType());
        createCell(row, currentCell++, contract.getAmount(), EDataFormat.CURRENCY);
        createCell(row, currentCell++, contract.getPlannedStartDate(), EDataFormat.DATE);
        createCell(row, currentCell++, contract.getPlannedEndDate(), EDataFormat.DATE);
        createCell(row, currentCell++, contract.getActualStartDate(), EDataFormat.DATE);
        createCell(row, currentCell++, contract.getActualEndDate(), EDataFormat.DATE);

        if (contract instanceof CounterpartyContractDtoExcel)
            createCell(row, currentCell, ((CounterpartyContractDtoExcel) contract).getContractDtoExcel().getName());
    }

    private void addContractStageToReport(ContractStageDtoExcel contractStageDtoExcel, Row row, Integer rowNUm) {
        int currentCell = 0;
        createCell(row, currentCell++, rowNUm);
        createCell(row, currentCell++, contractStageDtoExcel.getName());
        createCell(row, currentCell++, contractStageDtoExcel.getAmount(), EDataFormat.CURRENCY);
        createCell(row, currentCell++, contractStageDtoExcel.getPlannedStartDate(), EDataFormat.DATE);
        createCell(row, currentCell++, contractStageDtoExcel.getPlannedEndDate(), EDataFormat.DATE);
        createCell(row, currentCell++, contractStageDtoExcel.getActualStartDate(), EDataFormat.DATE);
        createCell(row, currentCell++, contractStageDtoExcel.getActualEndDate(), EDataFormat.DATE);
        createCell(row, currentCell++, contractStageDtoExcel.getPlannedMaterialCosts(), EDataFormat.CURRENCY);
        createCell(row, currentCell++, contractStageDtoExcel.getActualMaterialCosts(), EDataFormat.CURRENCY);
        createCell(row, currentCell++, contractStageDtoExcel.getPlannedSalaryExpenses(), EDataFormat.CURRENCY);
        createCell(row, currentCell, contractStageDtoExcel.getActualSalaryExpenses(), EDataFormat.CURRENCY);
    }

    private void addDefaultCellStyle(XSSFCellStyle cellStyle) {
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(EFont.TIMES_NEW_ROMAN.getFont(workbook, 12));
    }
    private void createCell(Row row, int i, Object value) {
        this.createCell(row, i, value, EDataFormat.NONE);
    }

    private void createCell(Row row, int i, Object value, EDataFormat dataFormat) {
        Cell cell = row.createCell(i);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        if (value != null) {
            if (value instanceof Double) {
                cell.setCellValue((Double) value);
                cellStyle.setDataFormat(
                        workbook.createDataFormat().getFormat(dataFormat.getFormat())
                );
            } else if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Integer){
                cell.setCellValue((Integer) value);
            }
        } else {
            cell.setCellValue("");
        }
        this.addDefaultCellStyle(cellStyle);
        cell.setCellStyle(cellStyle);
    }


    private void createTableTitle(String title, Row row) {
        mainSheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),row.getRowNum(),0,5));
        Cell cell = row.createCell(0);
        cell.setCellValue(title);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(EFont.TIMES_NEW_ROMAN.getFont(workbook, 20));
        cell.setCellStyle(cellStyle);

        CellRangeAddress region =
                new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 5);
        RegionUtil.setBorderRight(BorderStyle.THICK, region, mainSheet);
        RegionUtil.setBorderLeft(BorderStyle.THICK, region, mainSheet);
        RegionUtil.setBorderTop(BorderStyle.THICK, region, mainSheet);
    }

    private void createTableMarkup(Integer firstCol, Integer lastCol, Integer firstRow, Integer lastRow,
                                   boolean createFilterHeader) {
        AreaReference areaReferencer = workbook.getCreationHelper().createAreaReference(
                new CellReference(firstRow, firstCol), new CellReference(lastRow, lastCol));

        XSSFTable table = mainSheet.createTable(areaReferencer);
        table.getCTTable().addNewTableStyleInfo();
        XSSFTableStyleInfo style = (XSSFTableStyleInfo) table.getStyle();
        style.setName("TableStyleMedium2");
        style.setShowRowStripes(true);
        if (createFilterHeader) table.getCTTable().addNewAutoFilter().setRef(areaReferencer.formatAsString());
    }

    public void createColumnHeader(Row row, int i, EColumn column) {
        mainSheet.setColumnWidth(i, column.getColumnWidth());
        Cell cell = row.createCell(i);
        cell.setCellValue(column.getName());
        XSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(EFont.TIMES_NEW_ROMAN.getFont(workbook, 14));
        cell.setCellStyle(style);
    }
}
