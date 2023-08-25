package com.accountingsystem.controller;

import com.accountingsystem.controller.dtos.ContractDto;
import com.accountingsystem.controller.dtos.ContractStageDto;
import com.accountingsystem.controller.dtos.ContractUserDto;
import com.accountingsystem.controller.dtos.CounterpartyContractDto;
import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.controller.dtos.UserDto;

import com.accountingsystem.filters.ETargetEntity;
import com.accountingsystem.filters.SearchRequest;
import com.accountingsystem.service.AdminService;
import com.accountingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("api/admin/")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    //<editor-fold desc="Организации контр-агнеты">
    @PostMapping("counterparty-organizations")
    public ResponseEntity<Void> createCounterpartyOrganization(
            @RequestBody @Valid CounterpartyOrganizationDto counterpartyOrganizationDto) {
        adminService.createCounterpartyOrganization(counterpartyOrganizationDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("counterparty-organizations/{id}")
    public ResponseEntity<Void> updateCounterpartyOrganization(
            @RequestBody @Valid CounterpartyOrganizationDto counterpartyOrganizationDto,
            @PathVariable @Positive int id
    ) {
        adminService.updateCounterpartyOrganization(id, counterpartyOrganizationDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("counterparty-organizations/{id}")
    public ResponseEntity<Void> deleteCounterpartyOrganization(@PathVariable int id) {
        adminService.deleteCounterpartyOrganization(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    //</editor-fold>

    //<editor-fold desc="Контракты с контр-агентами">
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
    public ResponseEntity<Void> createCounterpartyContract(
            @PathVariable @Positive int contractId,
            @RequestBody @Valid CounterpartyContractDto counterpartyContractDto
    ) {
        adminService.createCounterpartyContract(contractId, counterpartyContractDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("counterparty-contracts/{id}")
    public ResponseEntity<Void> updateCounterpartyContractDto(
            @PathVariable @Positive int id,
            @RequestBody @Valid CounterpartyContractDto counterpartyContractDto
    ) {
        adminService.updateCounterpartyContract(id, counterpartyContractDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("counterparty-contracts/{id}")
    public ResponseEntity<Void> deleteCounterpartyContract(@PathVariable int id) {
        adminService.deleteCounterpartyContract(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    //</editor-fold>

    //<editor-fold desc="Стадии контракта">
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
    public ResponseEntity<Void> createContractStage(
            @PathVariable @Positive int contractId,
            @RequestBody @Valid ContractStageDto contractStageDto
    ) {
        adminService.createContractStage(contractId, contractStageDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("contract-stages/{id}")
    public ResponseEntity<Void> updateContractStage(
            @PathVariable @Positive int id,
            @RequestBody @Valid ContractStageDto contractStageDto
    ) {
        adminService.updateContractStage(id, contractStageDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("contract-stages/{id}")
    public ResponseEntity<Void> deleteContractStage(@PathVariable int id) {
        adminService.deleteContractStage(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    //</editor-fold>

    //<editor-fold desc="Контракт">
    // Так поскольку мы имеем не жесткую связь между контрактом и пользователем.
    // Контракт может быть изначально никому не назначен.

    @PostMapping("contracts/search")
    public ResponseEntity<Page<ContractUserDto>> getContractsWithUsers(
            @RequestBody @Valid SearchRequest searchRequest
    ) {
        Page<ContractUserDto> contracts = adminService.getContractWithUsers(searchRequest);
        return ResponseEntity.ok().body(contracts);
    }

    @PostMapping("contracts")
    public ResponseEntity<Void> createContract(
            @RequestBody @Valid ContractDto contractDto
    ) {
        adminService.createContract(contractDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("contracts/{id}")
    public ResponseEntity<Void> updateContract(
            @PathVariable @Positive int id,
            @RequestBody @Valid ContractDto contractDto
    ) {
        adminService.updateContract(id, contractDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("contracts/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable @Positive int id) {
        adminService.deleteContract(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    //</editor-fold>

    //<editor-fold desc="Пользователь">
    @PostMapping("users")
    public ResponseEntity<Page<UserDto>> getUsers(
            @RequestBody @Valid SearchRequest searchRequest
    ) {
        Page<UserDto> users = adminService.getAllUsers(searchRequest);
        return ResponseEntity.ok().body(users);
    }

    @PutMapping("users/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable @Positive Integer id, @RequestBody @Valid UserDto userDto) {
        adminService.updateUser(id, userDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive Integer id) {
        adminService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    //</editor-fold>
}
