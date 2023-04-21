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

@RestController
@RequestMapping("admin-api/")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    // ------------------------------------------- Организации контр-агенты -------------------------------------------

    @PostMapping("counterparty-organizations")
    public ResponseEntity<CounterpartyOrganizationDto> createCounterpartyOrganization(
            @RequestBody CounterpartyOrganizationDto counterpartyOrganizationDto) {
        adminService.createCounterpartyOrganization(counterpartyOrganizationDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("counterparty-organizations/{id}")
    public ResponseEntity<CounterpartyOrganizationDto> updateCounterpartyOrganization(
            @RequestBody CounterpartyOrganizationDto counterpartyOrganizationDto,
            @PathVariable int id
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

    @GetMapping("contracts/{contractId}/counterparty-contracts/")
    public ResponseEntity<Page<CounterpartyContractDto>> getCounterpartyContractsByContractId(
           @PathVariable Integer contractId,
           @RequestBody SearchRequest searchRequest
    ) {
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<CounterpartyContractDto> counterpartyContractDtos = userService.getCounterpartyContracts(searchRequest);
        return ResponseEntity.ok().body(counterpartyContractDtos);
    }

    @PostMapping("contracts/{contractId}/counterparty-contracts/")
    public ResponseEntity<CounterpartyContractDto> createCounterpartyContract(
            @PathVariable int contractId,
            @RequestBody CounterpartyContractDto counterpartyContractDto
    ) {
        adminService.createCounterpartyContract(contractId, counterpartyContractDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("counterparty-contracts/{id}")
    public ResponseEntity<CounterpartyContractDto> updateCounterpartyContractDto(
            @PathVariable int id,
            @RequestBody CounterpartyContractDto counterpartyContractDto
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

    @GetMapping("contracts/{contractId}/contract-stages/")
    public ResponseEntity<Page<ContractStageDto>> getContractStagesByContractId(
            @PathVariable Integer contractId,
            @RequestBody SearchRequest searchRequest
    ) {
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contractId);

        Page<ContractStageDto> contractStageDtos = userService.getContractStages(searchRequest);
        return ResponseEntity.ok().body(contractStageDtos);
    }

    @PostMapping("contracts/{contractId}/contract-stages")
    public ResponseEntity<ContractStageDto> createContractStage(
            @PathVariable int contractId,
            @RequestBody ContractStageDto contractStageDto
    ) {
        adminService.createContractStage(contractId, contractStageDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("contract-stages/{id}")
    public ResponseEntity<ContractStageDto> updateContractStage(
            @PathVariable int id,
            @RequestBody ContractStageDto contractStageDto
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

    @GetMapping("contracts/")
    public ResponseEntity<?> getContractsWithUsers(
            @RequestBody SearchRequest searchRequest
    ) {
        Page<ContractUserDto> contracts = adminService.getContractWithUsers(searchRequest);
        return ResponseEntity.ok().body(contracts);
    }

    @PostMapping("contracts/")
    public ResponseEntity<ContractDto> createContract(
            @RequestBody ContractDto contractDto
    ) {
        adminService.createContract(contractDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("contracts/{id}")
    public ResponseEntity<ContractDto> updateContract(
            @PathVariable int id,
            @RequestBody ContractDto contractDto
    ) {
        adminService.updateContract(id, contractDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("contracts/{id}")
    public ResponseEntity<ContractDto> deleteContract(@PathVariable int id) {
        adminService.deleteContract(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // ------------------------------------ Список пользователей ------------------------------------

    @GetMapping("users/")
    public ResponseEntity<?> getUsers(
            @RequestBody SearchRequest searchRequest
    ){
        Page<UserDto> users = adminService.getAllUsers(searchRequest);
        return ResponseEntity.ok().body(users);
    }
}
