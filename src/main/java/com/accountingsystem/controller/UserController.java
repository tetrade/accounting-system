package com.accountingsystem.controller;


import com.accountingsystem.dtos.ContractDto;
import com.accountingsystem.dtos.ContractStageDto;
import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.excel.ExcelReportWriter;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import com.accountingsystem.filters.*;
import com.accountingsystem.service.UserService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

@RequestMapping("api/")
@RestController
public class UserController {

    @Autowired
    private ExcelReportWriter excelReportWriter;

    @Autowired
    private UserService userService;

    // Методы для получения отчета пользователя
    @GetMapping("users/{login}/downland-contract-report/dates")
    public ResponseEntity<ByteArrayResource> downlandContractReport(
            @PathVariable String login,
            @RequestParam(value = "planned-start-date", defaultValue = "#{T(java.time.LocalDate).of(1000, 12, 31)}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate plannedStartDate,
            @RequestParam(value = "planned-end-date", defaultValue = "#{T(java.time.LocalDate).of(9999, 12, 31)}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate plannedEndDate
    ) throws IOException {

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

    @GetMapping("users/{login}/downland-contract-stage-report/{contractId}")
    public ResponseEntity<ByteArrayResource> downlandContractStageReport(
            @PathVariable String login,
            @PathVariable int contractId
    ) throws IOException {
        Set<ContractStageDtoExcel> contractStageDtoExcelSet =
                userService.getContractStagesContractForContractId(login, contractId);

        ByteArrayOutputStream stream =
                excelReportWriter.createContractStagesReport(contractStageDtoExcelSet);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=contract-stage-report.xlsx")
                .contentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new ByteArrayResource(stream.toByteArray()));
    }

    // TODO Заменить логин из URL на логин из Аунтефикейт

    // Методы для получения информации пользователя
    @GetMapping("counterparty-organizations/")
    public ResponseEntity<Page<CounterpartyOrganizationDto>> getCounterpartyOrganizations(@RequestBody SearchRequest searchRequest) {
        Page<CounterpartyOrganizationDto> counterpartyOrganizationDtos = userService.getCounterpartyOrganizations(searchRequest);
        return ResponseEntity.ok().body(counterpartyOrganizationDtos);
    }

    @GetMapping("users/{login}/contracts/")
    public ResponseEntity<Page<ContractDto>> getContracts(
            @PathVariable String login,
            @RequestBody SearchRequest searchRequest
    ) {

        searchRequest.addUserLoginFilter(login);

        Page<ContractDto> contractDtos = userService.getContracts(searchRequest);
        return ResponseEntity.ok().body(contractDtos);
    }

    @GetMapping("users/{login}/contracts/{contractId}/counterparty-contracts/")
    public ResponseEntity<Page<CounterpartyContractDto>> getCounterpartyContractsByContractId(
            @PathVariable String login, @PathVariable Integer contractId,
            @RequestBody SearchRequest searchRequest
    ) {
        searchRequest.addUserLoginFilter(login);
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<CounterpartyContractDto> counterpartyContractDtos = userService.getCounterpartyContracts(searchRequest);
        return ResponseEntity.ok().body(counterpartyContractDtos);
    }

    @GetMapping("users/{login}/contracts/{contractId}/contract-stages/")
    public ResponseEntity<Page<ContractStageDto>> getContractStagesByContractId(
            @PathVariable String login, @PathVariable Integer contractId,
            @RequestBody SearchRequest searchRequest
    ) {
        searchRequest.addUserLoginFilter(login);
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<ContractStageDto> contractStageDtos = userService.getContractStages(searchRequest);
        return ResponseEntity.ok().body(contractStageDtos);
    }
}
