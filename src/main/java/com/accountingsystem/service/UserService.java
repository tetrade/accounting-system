package com.accountingsystem.service;

import com.accountingsystem.controller.dtos.ContractDto;
import com.accountingsystem.controller.dtos.ContractStageDto;
import com.accountingsystem.controller.dtos.CounterpartyContractDto;
import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.controller.dtos.mappers.ContractMapper;
import com.accountingsystem.controller.dtos.mappers.ContractStageMapper;
import com.accountingsystem.controller.dtos.mappers.CounterpartyContractMapper;
import com.accountingsystem.controller.dtos.mappers.CounterpartyOrganizationMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class UserService {

    private final ContractRepo contractRepo;

    private final CounterpartyContractRepo counterpartyContractRepo;

    private final ContractStageMapper contractStageMapper;

    private final CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    private final ContractStageRepo contractStageRepo;

    private final ContractMapper contractMapper;

    private final CounterpartyContractMapper counterpartyContractMapper;

    private final CounterpartyOrganizationMapper counterpartyOrganizationMapper;

    @Autowired
    public UserService(ContractRepo contractRepo, CounterpartyContractRepo counterpartyContractRepo,
                       ContractStageMapper contractStageMapper, CounterpartyOrganizationRepo counterpartyOrganizationRepo,
                       ContractStageRepo contractStageRepo, CounterpartyContractMapper counterpartyContractMapper, ContractMapper contractMapper,
                       CounterpartyOrganizationMapper counterpartyOrganizationMapper) {
        this.contractRepo = contractRepo;
        this.contractStageMapper = contractStageMapper;
        this.counterpartyOrganizationRepo = counterpartyOrganizationRepo;
        this.counterpartyContractRepo = counterpartyContractRepo;
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

    // Методы для извлечения информации для пользователя с использованием фильтров и пагинации

    public Page<CounterpartyOrganizationDto> getCounterpartyOrganizations(SearchRequest searchRequest){
        SearchSpecification<CounterpartyOrganization> specification = new SearchSpecification<>(searchRequest);
        return counterpartyOrganizationRepo
                .findAll(specification, PageRequest.of(searchRequest.getPage(), searchRequest.getSize()))
                .map(counterpartyOrganizationMapper::map);
    }

    public Page<ContractDto> getContracts(SearchRequest searchRequest) {
        SearchSpecification<Contract> specification = new SearchSpecification<>(searchRequest);
        return contractRepo
                .findAll(specification, PageRequest.of(searchRequest.getPage(), searchRequest.getSize()))
                .map(contractMapper::mapToContractDto);
    }

    public Page<CounterpartyContractDto> getCounterpartyContracts(SearchRequest searchRequest) {
        SearchSpecification<CounterpartyContract> specification = new SearchSpecification<>(searchRequest);
        return counterpartyContractRepo
                .findAll(specification, PageRequest.of(searchRequest.getPage(), searchRequest.getSize()))
                .map(counterpartyContractMapper::mapToCounterpartyContractDto);
    }

    public Page<ContractStageDto> getContractStages(SearchRequest searchRequest) {
        SearchSpecification<ContractStage> specification = new SearchSpecification<>(searchRequest);
        return contractStageRepo
                .findAll(specification, PageRequest.of(searchRequest.getPage(), searchRequest.getSize()))
                .map(contractStageMapper::mapToContractStageDto);
    }
}
