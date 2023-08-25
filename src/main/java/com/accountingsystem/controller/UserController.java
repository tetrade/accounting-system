package com.accountingsystem.controller;


import com.accountingsystem.controller.dtos.*;
import com.accountingsystem.controller.dtos.mappers.UserMapper;
import com.accountingsystem.excel.ExcelReportWriter;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import com.accountingsystem.filters.ETargetEntity;
import com.accountingsystem.filters.SearchRequest;
import com.accountingsystem.service.UserDetailsImpl;
import com.accountingsystem.service.UserService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

@RequestMapping("api/user/")
@RestController
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class UserController {


    private final UserService userService;
    private final UserMapper userMapper;
    private final ExcelReportWriter excelReportWriter;


    public UserController(UserService userService, UserMapper userMapper, ExcelReportWriter excelReportWriter) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.excelReportWriter = excelReportWriter;
    }

    //<editor-fold desc="Методы для получения информации пользователя">
    @PostMapping("counterparty-organizations/search")
    public ResponseEntity<Page<CounterpartyOrganizationDto>> getCounterpartyOrganizations(@RequestBody SearchRequest searchRequest) {
        Page<CounterpartyOrganizationDto> counterpartyOrganizationDtos = userService.getCounterpartyOrganizations(searchRequest);
        return ResponseEntity.ok().body(counterpartyOrganizationDtos);
    }


    @PostMapping("contracts/search")
    public ResponseEntity<Page<ContractDto>> getContracts(
            @RequestBody @Valid SearchRequest searchRequest,
            Authentication authentication
    ) {

        searchRequest.addUserLoginFilter(authentication.getName());
        Page<ContractDto> contractDtos = userService.getContracts(searchRequest);
        return ResponseEntity.ok().body(contractDtos);
    }

    @PostMapping("contracts/{contractId}/counterparty-contracts/search")
    public ResponseEntity<Page<CounterpartyContractDto>> getCounterpartyContractsByContractId(
            @PathVariable @Positive Integer contractId,
            @RequestBody @Valid SearchRequest searchRequest,
            Authentication authentication
    ) {
        searchRequest.addUserLoginFilter(authentication.getName());
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<CounterpartyContractDto> counterpartyContractDtos = userService.getCounterpartyContracts(searchRequest);
        return ResponseEntity.ok().body(counterpartyContractDtos);
    }

    @PostMapping("contracts/{contractId}/contract-stages/search")
    public ResponseEntity<Page<ContractStageDto>> getContractStagesByContractId(
            @PathVariable @Positive Integer contractId,
            @RequestBody @Valid SearchRequest searchRequest,
            Authentication authentication
    ) {
        searchRequest.addUserLoginFilter(authentication.getName());
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<ContractStageDto> contractStageDtos = userService.getContractStages(searchRequest);
        return ResponseEntity.ok().body(contractStageDtos);
    }

    @PostMapping("/user-info")
    public ResponseEntity<UserDto> getUserInfo(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(userMapper.mapToUserDto(userDetails));
    }
    //</editor-fold>


    @GetMapping("downland-contract-report/dates")
    public ResponseEntity<ByteArrayResource> downlandContractReport(
            @RequestParam(value = "plannedStartDate", defaultValue = "#{T(java.time.LocalDate).of(1000, 12, 31)}") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate plannedStartDate,
            @RequestParam(value = "plannedEndDate", defaultValue = "#{T(java.time.LocalDate).of(9999, 12, 31)}") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate plannedEndDate,
            Authentication authentication
    ) throws IOException {
        String login = authentication.getName();
        Set<ContractDtoExcel> contractDtoExcelSet =
                userService.getContractsBetweenDates(login, plannedStartDate, plannedEndDate);

        Set<CounterpartyContractDtoExcel> counterpartyContractDtoExcelSet =
                userService.getCounterpartyContractsBetweenDates(login, plannedStartDate, plannedEndDate);

        ByteArrayOutputStream stream =
                excelReportWriter.createContractReport(counterpartyContractDtoExcelSet, contractDtoExcelSet);

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
