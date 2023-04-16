package com.accountingsystem.service;

import com.accountingsystem.dtos.ContractDto;
import com.accountingsystem.dtos.ContractStageDto;
import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private CustomAdminRepo customAdminRepo;

    private final ContractRepo contractRepo;

    private final CounterpartyContractRepo counterpartyContractRepo;

    private final CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    private final ContractStageRepo contractStageRepo;


    @Autowired
    public AdminService(ContractRepo contractRepo, CounterpartyContractRepo counterpartyContractRepo,
                       CounterpartyOrganizationRepo counterpartyOrganizationRepo, ContractStageRepo contractStageRepo) {
        this.contractRepo = contractRepo;
        this.counterpartyOrganizationRepo = counterpartyOrganizationRepo;
        this.counterpartyContractRepo = counterpartyContractRepo;
        this.contractStageRepo = contractStageRepo;
    }


    // Удаление/добавление/изменение организаций контр-агентов

    public void createCounterpartyOrganization(CounterpartyOrganizationDto counterpartyOrganizationDto) {
        customAdminRepo.insertCounterpartyOrganization(counterpartyOrganizationDto);
    }


    public void updateCounterpartyOrganization(
            int id, CounterpartyOrganizationDto counterpartyOrganizationDto
    ) {
        if(!counterpartyOrganizationRepo.existsById(id)) throw new RuntimeException(); // TODO()
        customAdminRepo.updateCounterpartyOrganization(id, counterpartyOrganizationDto);
    }

    public void deleteCounterpartyOrganization(int id) {
        if(!counterpartyOrganizationRepo.existsById(id)) throw new RuntimeException(); // TODO()
        counterpartyOrganizationRepo.deleteById(id);
    }


    // Удаление-добавление-изменение стадий контрактов

    public void createContractStage(int contractId, ContractStageDto contractStageDto) {
        customAdminRepo.insertContractStage(contractId, contractStageDto);
    }

    public void updateContractStage(int id, ContractStageDto contractStageDto) {
        if (!contractStageRepo.existsById(id)) throw new RuntimeException(); // TODO()
        customAdminRepo.updateContractStage(id, contractStageDto);
    }

    public void deleteContractStage(int id) {
        if (!contractStageRepo.existsById(id)) throw new RuntimeException(); // TODO()
        contractStageRepo.deleteById(id);
    }

    // Удаление-добавление-изменение контр-контрактов
    public void createCounterpartyContract(
            int contractId, CounterpartyContractDto counterpartyContractDto) {
        customAdminRepo.insertCounterpartyContract(contractId, counterpartyContractDto);
    }

    public void updateCounterpartyContract(int id, CounterpartyContractDto counterpartyContractDto) {
        if (!counterpartyContractRepo.existsById(id)) throw new RuntimeException(); // TODO()
        customAdminRepo.updateCounterpartyContract(id, counterpartyContractDto);
    }

    public void deleteCounterpartyContract(int id) {
        if (!counterpartyContractRepo.existsById(id)) throw new RuntimeException(); // TODO()
        counterpartyContractRepo.deleteById(id);
    }
    // ------------------------------------- Контракт ------------------------------------------

    public void createContract(int userId, ContractDto contractDto) {
        customAdminRepo.insertContract(userId, contractDto);
    }

    public void updateContract(int contractId, ContractDto contractDto) {
        if (!contractRepo.existsById(contractId)) throw new RuntimeException(); // TODO()
        customAdminRepo.updateContract(contractId, contractDto);
    }

    public void deleteContract(int contractId) {
        if (!contractRepo.existsById(contractId)) throw new RuntimeException(); // TODO()
        contractRepo.deleteById(contractId);
    }
}




















