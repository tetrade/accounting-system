package com.accountingsystem.service.user;

import com.accountingsystem.dtos.mappers.ContractMapper;
import com.accountingsystem.dtos.mappers.ContractStageMapper;
import com.accountingsystem.dtos.mappers.CounterpartyContractMapper;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.ContractStage;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class UserService {

    private final ContractRepo contractRepo;

    private final CounterpartyContractRepo counterpartyContractRepo;

    private final ContractStageMapper contractStageMapper;

    private final CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    private final UserRepo userRepo;

    private final ContractStageRepo contractStageRepo;

    private final ContractMapper contractMapper;

    private final CounterpartyContractMapper counterpartyContractMapper;

    @Autowired
    public UserService(ContractRepo contractRepo, CounterpartyContractRepo counterpartyContractRepo,
                       ContractStageMapper contractStageMapper, CounterpartyOrganizationRepo counterpartyOrganizationRepo, UserRepo userRepo,
                       ContractStageRepo contractStageRepo, CounterpartyContractMapper counterpartyContractMapper, ContractMapper contractMapper
    ) {
        this.contractRepo = contractRepo;
        this.contractStageMapper = contractStageMapper;
        this.counterpartyOrganizationRepo = counterpartyOrganizationRepo;
        this.counterpartyContractRepo = counterpartyContractRepo;
        this.userRepo = userRepo;
        this.contractStageRepo = contractStageRepo;
        this.contractMapper = contractMapper;
        this.counterpartyContractMapper = counterpartyContractMapper;
    }

    public Set<ContractDtoExcel> getAllContractsContractsBetweenDates(
            String login, LocalDate startDate, LocalDate endDate
    ) {
        Set<Contract> contractsBetweenDates = contractRepo.getContractsBetweenDatesByLogin(login, startDate, endDate);
        return contractMapper.mapToContractDtoExcelSet(contractsBetweenDates);
    }


    public Set<CounterpartyContractDtoExcel> getCounterpartyContractsBetweenDates(
            String login, LocalDate startDate, LocalDate endDate
    ) {
        Set<CounterpartyContract> counterpartyContractsBetweenDates =
                counterpartyContractRepo.getCounterpartyContractsBetweenDatesByLogin(login, startDate, endDate);
        return counterpartyContractMapper
                .mapToCounterpartyContractsDtoExcelSet(counterpartyContractsBetweenDates);
    }

    public Set<ContractStageDtoExcel> getContractStagesContractForContractId(
            String login, Integer contractId
    ) {
        Set<ContractStage> contractStages =
                contractStageRepo.getContractStagesByContractIdAndUserLogin(login, contractId);
        return contractStageMapper.mapToContractStageDtoExcelSet(contractStages);
    }
}
