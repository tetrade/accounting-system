package com.accountingsystem.controller;

import com.accountingsystem.dtos.ContractDto;
import com.accountingsystem.dtos.ContractStageDto;
import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
public class AdminController {

    @Autowired
    private AdminService adminService;

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

    @PostMapping("contracts/{contractId}/contract-stages")
    public ResponseEntity<ContractStageDto> createContractStage(
            @PathVariable int contractId,
            @RequestBody ContractStageDto contractStageDto
    ) {
        adminService.createContractStage(contractId, contractStageDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("contract-stage/{id}")
    public ResponseEntity<ContractStageDto> updateContractStage(
            @PathVariable int id,
            @RequestBody ContractStageDto contractStageDto
    ) {
        adminService.updateContractStage(id, contractStageDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("contract-stage/{id}")
    public ResponseEntity<ContractStageDto> deleteContractStage(@PathVariable int id) {
        adminService.deleteContractStage(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    --------------------------- Контракты ---------------------------

    @PostMapping("/contracts/")
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
}
