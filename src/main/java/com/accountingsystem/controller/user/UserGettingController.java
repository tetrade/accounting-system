package com.accountingsystem.controller.user;


import com.accountingsystem.controller.dtos.ContractDto;
import com.accountingsystem.controller.dtos.ContractStageDto;
import com.accountingsystem.controller.dtos.CounterpartyContractDto;
import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.excel.ExcelReportWriter;
import com.accountingsystem.filters.*;
import com.accountingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("user-api/")
@RestController
public class UserGettingController {

    @Autowired
    private ExcelReportWriter excelReportWriter;

    @Autowired
    private UserService userService;

    // TODO Заменить логин из URL на логин из Аунтефикейт

    // Методы для получения информации пользователя
    @GetMapping("counterparty-organizations/")
    public ResponseEntity<Page<CounterpartyOrganizationDto>> getCounterpartyOrganizations(@RequestBody SearchRequest searchRequest) {
        Page<CounterpartyOrganizationDto> counterpartyOrganizationDtos = userService.getCounterpartyOrganizations(searchRequest);
        return ResponseEntity.ok().body(counterpartyOrganizationDtos);
    }

    @GetMapping("{login}/contracts/")
    public ResponseEntity<Page<ContractDto>> getContracts(
            @PathVariable String login,
            @RequestBody SearchRequest searchRequest
    ) {

        searchRequest.addUserLoginFilter(login);

        Page<ContractDto> contractDtos = userService.getContracts(searchRequest);
        return ResponseEntity.ok().body(contractDtos);
    }

    @GetMapping("{login}/contracts/{contractId}/counterparty-contracts/")
    public ResponseEntity<Page<CounterpartyContractDto>> getCounterpartyContractsByContractId(
            @PathVariable String login, @PathVariable Integer contractId,
            @RequestBody SearchRequest searchRequest
    ) {
        searchRequest.addUserLoginFilter(login);
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<CounterpartyContractDto> counterpartyContractDtos = userService.getCounterpartyContracts(searchRequest);
        return ResponseEntity.ok().body(counterpartyContractDtos);
    }

    @GetMapping("{login}/contracts/{contractId}/contract-stages/")
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
