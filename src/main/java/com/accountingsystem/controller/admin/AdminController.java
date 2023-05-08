package com.accountingsystem.controller.admin;

import com.accountingsystem.controller.dtos.*;
import com.accountingsystem.filters.ETargetEntity;
import com.accountingsystem.filters.SearchRequest;
import com.accountingsystem.service.AdminService;
import com.accountingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("api/admin/")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    // ------------------------------------------- Организации контр-агенты -------------------------------------------

    @PostMapping("counterparty-organizations")
    public ResponseEntity<CounterpartyOrganizationDto> createCounterpartyOrganization(
            @RequestBody @Valid CounterpartyOrganizationDto counterpartyOrganizationDto) {
        adminService.createCounterpartyOrganization(counterpartyOrganizationDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("counterparty-organizations/{id}")
    public ResponseEntity<CounterpartyOrganizationDto> updateCounterpartyOrganization(
            @RequestBody @Valid CounterpartyOrganizationDto counterpartyOrganizationDto,
            @PathVariable @Positive int id
    ) {
        adminService.updateCounterpartyOrganization(id, counterpartyOrganizationDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("counterparty-organizations/{id}")
    public ResponseEntity<CounterpartyOrganizationDto> deleteCounterpartyOrganization(@PathVariable int id) {
        adminService.deleteCounterpartyOrganization(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // --------------------------------------- Контракты с контр-агентами ---------------------------------------

    @PostMapping("contracts/{contractId}/counterparty-contracts/search")
    public ResponseEntity<Page<CounterpartyContractDto>> getCounterpartyContractsByContractId(
           @PathVariable @Positive Integer contractId,
           @RequestBody @Valid SearchRequest searchRequest
    ) {
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<CounterpartyContractDto> counterpartyContractDtos = userService.getCounterpartyContracts(searchRequest);
        return ResponseEntity.ok().body(counterpartyContractDtos);
    }

    @PostMapping("contracts/{contractId}/counterparty-contracts")
    public ResponseEntity<CounterpartyContractDto> createCounterpartyContract(
            @PathVariable @Positive int contractId,
            @RequestBody @Valid CounterpartyContractDto counterpartyContractDto
    ) {
        adminService.createCounterpartyContract(contractId, counterpartyContractDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("counterparty-contracts/{id}")
    public ResponseEntity<CounterpartyContractDto> updateCounterpartyContractDto(
            @PathVariable @Positive int id,
            @RequestBody @Valid CounterpartyContractDto counterpartyContractDto
    ) {
        adminService.updateCounterpartyContract(id, counterpartyContractDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("counterparty-contracts/{id}")
    public ResponseEntity<CounterpartyContractDto> deleteCounterpartyContract(@PathVariable int id) {
        adminService.deleteCounterpartyContract(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // ------------------------------------- Стадии контракта -------------------------------------

    @PostMapping("contracts/{contractId}/contract-stages/search")
    public ResponseEntity<Page<ContractStageDto>> getContractStagesByContractId(
            @PathVariable @Positive Integer contractId,
            @RequestBody @Valid SearchRequest searchRequest
    ) {
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<ContractStageDto> contractStageDtos = userService.getContractStages(searchRequest);
        return ResponseEntity.ok().body(contractStageDtos);
    }

    @PostMapping("contracts/{contractId}/contract-stages")
    public ResponseEntity<ContractStageDto> createContractStage(
            @PathVariable @Positive int contractId,
            @RequestBody @Valid ContractStageDto contractStageDto
    ) {
        adminService.createContractStage(contractId, contractStageDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("contract-stages/{id}")
    public ResponseEntity<ContractStageDto> updateContractStage(
            @PathVariable @Positive int id,
            @RequestBody @Valid ContractStageDto contractStageDto
    ) {
        adminService.updateContractStage(id, contractStageDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("contract-stages/{id}")
    public ResponseEntity<ContractStageDto> deleteContractStage(@PathVariable int id) {
        adminService.deleteContractStage(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    ----------------------------------------------- Контракты -----------------------------------------------

    // Так поскольку мы имеем не жесткую связь между контрактом и пользователем.
    // Контракт может быть изначально никому не назначен.

    @PostMapping("contracts/search")
    public ResponseEntity<?> getContractsWithUsers(
            @RequestBody @Valid SearchRequest searchRequest
    ) {
        Page<ContractUserDto> contracts = adminService.getContractWithUsers(searchRequest);
        return ResponseEntity.ok().body(contracts);
    }

    @PostMapping("contracts")
    public ResponseEntity<ContractDto> createContract(
            @RequestBody @Valid ContractDto contractDto
    ) {
        adminService.createContract(contractDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("contracts/{id}")
    public ResponseEntity<ContractDto> updateContract(
            @PathVariable @Positive int id,
            @RequestBody @Valid ContractDto contractDto
    ) {
        adminService.updateContract(id, contractDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("contracts/{id}")
    public ResponseEntity<ContractDto> deleteContract(@PathVariable @Positive    int id) {
        adminService.deleteContract(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // ------------------------------------ Список пользователей ------------------------------------

    @PostMapping("users")
    public ResponseEntity<?> getUsers(
            @RequestBody @Valid SearchRequest searchRequest
    ){
        Page<UserDto> users = adminService.getAllUsers(searchRequest);
        return ResponseEntity.ok().body(users);
    }
}
