package com.accountingsystem.service.user;

import com.accountingsystem.dtos.ContractDto;
import com.accountingsystem.dtos.ContractStageDto;
import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.dtos.mappers.ContractMapper;
import com.accountingsystem.dtos.mappers.ContractStageMapper;
import com.accountingsystem.dtos.mappers.CounterpartyContractMapper;
import com.accountingsystem.dtos.mappers.CounterpartyOrganizationMapper;
import com.accountingsystem.entitys.CounterpartyOrganization;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.ContractStage;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.filters.SearchRequest;
import com.accountingsystem.filters.SearchSpecification;
import com.accountingsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

    private final CounterpartyOrganizationMapper counterpartyOrganizationMapper;

    @Autowired
    public UserService(ContractRepo contractRepo, CounterpartyContractRepo counterpartyContractRepo,
                       ContractStageMapper contractStageMapper, CounterpartyOrganizationRepo counterpartyOrganizationRepo, UserRepo userRepo,
                       ContractStageRepo contractStageRepo, CounterpartyContractMapper counterpartyContractMapper, ContractMapper contractMapper,
                       CounterpartyOrganizationMapper counterpartyOrganizationMapper) {
        this.contractRepo = contractRepo;
        this.contractStageMapper = contractStageMapper;
        this.counterpartyOrganizationRepo = counterpartyOrganizationRepo;
        this.counterpartyContractRepo = counterpartyContractRepo;
        this.userRepo = userRepo;
        this.contractStageRepo = contractStageRepo;
        this.contractMapper = contractMapper;
        this.counterpartyContractMapper = counterpartyContractMapper;
        this.counterpartyOrganizationMapper = counterpartyOrganizationMapper;
    }


    // Методы для извлечения данных для составления отчета

    public Set<ContractDtoExcel> getContractsBetweenDates(
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

    // Методы для извлечения информации для пользователя с использованием фильтров

    public Set<CounterpartyOrganizationDto> getCounterpartyOrganizations(SearchRequest searchRequest){
        SearchSpecification<CounterpartyOrganization> specification = new SearchSpecification<>(searchRequest);
        List<CounterpartyOrganization> counterpartyOrganizations = counterpartyOrganizationRepo.findAll(specification);
        return counterpartyOrganizationMapper.map(counterpartyOrganizations);
    }

    public Set<ContractDto> getContracts(SearchRequest searchRequest) {
        SearchSpecification<Contract> specification = new SearchSpecification<>(searchRequest);
        List<Contract> contracts = contractRepo.findAll(specification);
        return contractMapper.mapToContractDtoSet(contracts);
    }

    public Set<CounterpartyContractDto> getCounterpartyContracts(SearchRequest searchRequest) {
        SearchSpecification<CounterpartyContract> specification = new SearchSpecification<>(searchRequest);
        List<CounterpartyContract> counterpartyContracts = counterpartyContractRepo.findAll(specification);
        return counterpartyContractMapper.mapToCounterpartyContractDtoSet(counterpartyContracts);
    }

    public Set<ContractStageDto> getContractStages(SearchRequest searchRequest) {
        SearchSpecification<ContractStage> specification = new SearchSpecification<>(searchRequest);
        List<ContractStage> contractStages = contractStageRepo.findAll(specification);
        return contractStageMapper.mapToContractStageDtoSet(contractStages);
    }
}
