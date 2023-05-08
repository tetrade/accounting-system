package com.accountingsystem.controller.user;

import com.accountingsystem.excel.ExcelReportWriter;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import com.accountingsystem.service.UserService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

@RequestMapping("api/user/")
@RestController
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class UserExcelController {
    @Autowired
    private ExcelReportWriter excelReportWriter;

    @Autowired
    private UserService userService;

    @GetMapping("downland-contract-report/dates")
    public ResponseEntity<ByteArrayResource> downlandContractReport(
            @RequestParam(value = "planned-start-date", defaultValue = "#{T(java.time.LocalDate).of(1000, 12, 31)}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate plannedStartDate,
            @RequestParam(value = "planned-end-date", defaultValue = "#{T(java.time.LocalDate).of(9999, 12, 31)}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate plannedEndDate,
            Authentication authentication
    ) throws IOException {
        String login = authentication.getName();
        Set<ContractDtoExcel> contractDtoExcelSet =
                userService.getContractsBetweenDates(login, plannedStartDate, plannedEndDate);

        Set<CounterpartyContractDtoExcel> counterpartyContractDtoExcelSet =
                userService.getCounterpartyContractsBetweenDates(login, plannedStartDate, plannedEndDate);

        ByteArrayOutputStream stream =
                excelReportWriter.createContractReprot(counterpartyContractDtoExcelSet, contractDtoExcelSet);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=contract-report.xlsx")
                .contentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new ByteArrayResource(stream.toByteArray()));
    }

    @GetMapping("downland-contract-stage-report/{contractId}")
    public ResponseEntity<ByteArrayResource> downlandContractStageReport(
            Authentication authentication,
            @PathVariable @Positive int contractId
    ) throws IOException {
        String login = authentication.getName();
        Set<ContractStageDtoExcel> contractStageDtoExcelSet =
                userService.getContractStagesContractForContractId(login, contractId);

        ByteArrayOutputStream stream =
                excelReportWriter.createContractStagesReport(contractStageDtoExcelSet);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=contract-stage-report.xlsx")
                .contentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new ByteArrayResource(stream.toByteArray()));
    }
}
