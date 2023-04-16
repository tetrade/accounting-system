package com.accountingsystem.service;

import com.accountingsystem.advice.exceptions.NoSuchRowException;
import com.accountingsystem.dtos.ContractDto;
import com.accountingsystem.dtos.ContractStageDto;
import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AdminService {

    @Autowired
    private CustomAdminRepo customAdminRepo;

    private final ContractRepo contractRepo;

    private final CounterpartyContractRepo counterpartyContractRepo;

    private final CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    private final ContractStageRepo contractStageRepo;

    private final UserRepo userRepo;


    @Autowired
    public AdminService(ContractRepo contractRepo, CounterpartyContractRepo counterpartyContractRepo,
                       CounterpartyOrganizationRepo counterpartyOrganizationRepo, ContractStageRepo contractStageRepo,
                        UserRepo userRepo) {
        this.contractRepo = contractRepo;
        this.counterpartyOrganizationRepo = counterpartyOrganizationRepo;
        this.counterpartyContractRepo = counterpartyContractRepo;
        this.contractStageRepo = contractStageRepo;
        this.userRepo = userRepo;
    }

    // Удаление/добавление/изменение организаций контр-агентов

    public void createCounterpartyOrganization(CounterpartyOrganizationDto counterpartyOrganizationDto) {
        customAdminRepo.insertCounterpartyOrganization(counterpartyOrganizationDto);
    }

    public void updateCounterpartyOrganization(
            int id, CounterpartyOrganizationDto counterpartyOrganizationDto
    ) {
        if(!counterpartyOrganizationRepo.existsById(id))
            throw new NoSuchRowException("id", id, "CounterpartyOrganization");

        customAdminRepo.updateCounterpartyOrganization(id, counterpartyOrganizationDto);
    }

    public void deleteCounterpartyOrganization(int id) {
        if(!counterpartyOrganizationRepo.existsById(id))
            throw new NoSuchRowException("id", id, "Counterparty organization");

        counterpartyOrganizationRepo.deleteById(id);
    }

    // Удаление-добавление-изменение стадий контрактов

    public void createContractStage(int contractId, ContractStageDto contractStageDto) {
        if (!contractRepo.existsById(contractId))
            throw new NoSuchRowException("id", contractId, "Contract");

        customAdminRepo.insertContractStage(contractId, contractStageDto);
    }

    public void updateContractStage(int id, ContractStageDto contractStageDto) {
        if (!contractStageRepo.existsById(id))
            throw new NoSuchRowException("id", id, "Contract stage");

        customAdminRepo.updateContractStage(id, contractStageDto);
    }

    public void deleteContractStage(int id) {
        if (!contractStageRepo.existsById(id))
            throw new NoSuchRowException("id", id, "Contract stage");

        contractStageRepo.deleteById(id);
    }

    // Удаление-добавление-изменение контр-контрактов
    public void createCounterpartyContract(
            int contractId, CounterpartyContractDto counterpartyContractDto) {
        if (!counterpartyContractRepo.existsById(contractId))
            throw new NoSuchRowException("id", contractId, "Contract");
        Integer counterpartyOrganizationId = counterpartyContractDto.getCounterpartyOrganizationId();
        if (counterpartyOrganizationId != null && !counterpartyOrganizationRepo.existsById(counterpartyOrganizationId))
            throw new NoSuchRowException("id", counterpartyOrganizationId, "Counterparty organization");

        customAdminRepo.insertCounterpartyContract(contractId, counterpartyContractDto);
    }

    public void updateCounterpartyContract(int id, CounterpartyContractDto counterpartyContractDto) {
        if (!counterpartyContractRepo.existsById(id))
            throw new NoSuchRowException("id", id, "Counterparty contract");
        Integer counterpartyOrganizationId = counterpartyContractDto.getCounterpartyOrganizationId();
        if (counterpartyOrganizationId != null && !counterpartyOrganizationRepo.existsById(counterpartyOrganizationId))
            throw new NoSuchRowException("id", counterpartyOrganizationId, "Counterparty organization");

        customAdminRepo.updateCounterpartyContract(id, counterpartyContractDto);
    }

    public void deleteCounterpartyContract(Integer id) {
        if (!counterpartyContractRepo.existsById(id))
            throw new NoSuchRowException("id", id, "Counterparty contract");

        counterpartyContractRepo.deleteById(id);
    }
    // ------------------------------------- Контракт ------------------------------------------

    public void createContract(ContractDto contractDto) {
        Integer userId = contractDto.getUserId();
        if (userId != null && !userRepo.existsById(userId))
            throw new NoSuchRowException("id", userId, "User");
        contractDto.setUserId(userId);

        customAdminRepo.insertContract(contractDto);
    }

    public void updateContract(Integer contractId, ContractDto contractDto) {
        if (!contractRepo.existsById(contractId))
            throw new  NoSuchRowException("id", contractId, "Contract");
        Integer userId = contractDto.getUserId();
        if (userId != null && !userRepo.existsById(userId))
            throw new NoSuchRowException("id", userId, "User");
        contractDto.setUserId(userId);

        customAdminRepo.updateContract(contractId, contractDto);
    }

    public void deleteContract(Integer contractId) {
        if (!contractRepo.existsById(contractId))
            throw new  NoSuchRowException("id", contractId, "Contract");

        contractRepo.deleteById(contractId);
    }
}




















