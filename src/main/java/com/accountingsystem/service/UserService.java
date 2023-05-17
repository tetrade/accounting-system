package com.accountingsystem.service;

import com.accountingsystem.entitys.UserLog;
import com.accountingsystem.controller.dtos.SignUpRequest;
import com.accountingsystem.controller.dtos.ContractDto;
import com.accountingsystem.controller.dtos.ContractStageDto;
import com.accountingsystem.controller.dtos.CounterpartyContractDto;
import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.controller.dtos.mappers.*;
import com.accountingsystem.entitys.*;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import com.accountingsystem.filters.SearchRequest;
import com.accountingsystem.filters.SearchSpecification;
import com.accountingsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private final UserRepo userRepo;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepo roleRepo;

    private final UserLogRepository userLogRepository;

    @Autowired
    public UserService(ContractRepo contractRepo, CounterpartyContractRepo counterpartyContractRepo,
                       ContractStageMapper contractStageMapper, CounterpartyOrganizationRepo counterpartyOrganizationRepo,
                       ContractStageRepo contractStageRepo, CounterpartyContractMapper counterpartyContractMapper, ContractMapper contractMapper,
                       CounterpartyOrganizationMapper counterpartyOrganizationMapper, UserRepo userRepo, UserMapper userMapper, PasswordEncoder passwordEncoder, RoleRepo roleRepo, UserLogRepository userLogRepository) {
        this.contractRepo = contractRepo;
        this.contractStageMapper = contractStageMapper;
        this.counterpartyOrganizationRepo = counterpartyOrganizationRepo;
        this.counterpartyContractRepo = counterpartyContractRepo;
        this.contractStageRepo = contractStageRepo;
        this.contractMapper = contractMapper;
        this.counterpartyContractMapper = counterpartyContractMapper;
        this.counterpartyOrganizationMapper = counterpartyOrganizationMapper;
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.userLogRepository = userLogRepository;
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
        return contractMapper
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

    public void createNewUser(SignUpRequest signupRequest) {
        User user = userMapper.mapToUser(signupRequest, passwordEncoder, roleRepo);
        userRepo.save(user);
    }

    public void logUserLogin(String login, String ip) {
        UserLog userLog = new UserLog();
        userLog.setLogin(login);
        userLog.setIpAddress(ip);
        userLog.setTime(LocalDateTime.now());
        userLogRepository.save(userLog);
    }
}
