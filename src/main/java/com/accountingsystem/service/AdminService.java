package com.accountingsystem.service;

import com.accountingsystem.advice.exceptions.IllegalFieldValueException;
import com.accountingsystem.advice.exceptions.NoSuchRowException;
import com.accountingsystem.controller.dtos.*;
import com.accountingsystem.controller.dtos.mappers.ContractMapper;
import com.accountingsystem.controller.dtos.mappers.UserMapper;
import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.User;
import com.accountingsystem.entitys.enums.ERole;
import com.accountingsystem.filters.SearchRequest;
import com.accountingsystem.filters.SearchSpecification;
import com.accountingsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final ContractRepo contractRepo;

    private final CounterpartyContractRepo counterpartyContractRepo;

    private final CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    private final ContractStageRepo contractStageRepo;

    private final UserRepo userRepo;

    private final UserMapper userMapper;

    private final ContractMapper contractMapper;

    @Autowired
    public AdminService(ContractRepo contractRepo, CounterpartyContractRepo counterpartyContractRepo,
                       CounterpartyOrganizationRepo counterpartyOrganizationRepo, ContractStageRepo contractStageRepo,
                        UserRepo userRepo, UserMapper userMapper, ContractMapper contractMapper) {
        this.contractRepo = contractRepo;
        this.counterpartyOrganizationRepo = counterpartyOrganizationRepo;
        this.counterpartyContractRepo = counterpartyContractRepo;
        this.contractStageRepo = contractStageRepo;
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.contractMapper= contractMapper;
    }

    // Удаление/добавление/изменение организаций контр-агентов

    public void createCounterpartyOrganization(CounterpartyOrganizationDto counterpartyOrganizationDto) {
        counterpartyOrganizationRepo.insertCounterpartyOrganization(counterpartyOrganizationDto);
    }

    public void updateCounterpartyOrganization(
            int id, CounterpartyOrganizationDto counterpartyOrganizationDto
    ) {
        if(!counterpartyOrganizationRepo.existsById(id))
            throw new NoSuchRowException("id", id, "CounterpartyOrganization");

        counterpartyOrganizationRepo.updateCounterpartyOrganization(id, counterpartyOrganizationDto);
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

        contractStageRepo.insertContractStage(contractId, contractStageDto);
    }

    public void updateContractStage(int id, ContractStageDto contractStageDto) {
        if (!contractStageRepo.existsById(id))
            throw new NoSuchRowException("id", id, "Contract stage");

        contractStageRepo.updateContractStage(id, contractStageDto);
    }

    public void deleteContractStage(int id) {
        if (!contractStageRepo.existsById(id))
            throw new NoSuchRowException("id", id, "Contract stage");

        contractStageRepo.deleteById(id);
    }

    // Удаление-добавление-изменение контр-контрактов

    public void createCounterpartyContract(
            int contractId, CounterpartyContractDto counterpartyContractDto) {
        if (!contractRepo.existsById(contractId))
            throw new NoSuchRowException("id", contractId, "Contract");
        Integer counterpartyOrganizationId = counterpartyContractDto.getCounterpartyOrganizationId();
        if (counterpartyOrganizationId != null && !counterpartyOrganizationRepo.existsById(counterpartyOrganizationId))
            throw new NoSuchRowException("id", counterpartyOrganizationId, "Counterparty organization");

        counterpartyContractRepo.insertCounterpartyContract(contractId, counterpartyContractDto);
    }

    public void updateCounterpartyContract(int id, CounterpartyContractDto counterpartyContractDto) {
        if (!counterpartyContractRepo.existsById(id))
            throw new NoSuchRowException("id", id, "Counterparty contract");
        Integer counterpartyOrganizationId = counterpartyContractDto.getCounterpartyOrganizationId();
        if (counterpartyOrganizationId != null && !counterpartyOrganizationRepo.existsById(counterpartyOrganizationId))
            throw new NoSuchRowException("id", counterpartyOrganizationId, "Counterparty organization");

        counterpartyContractRepo.updateCounterpartyContract(id, counterpartyContractDto);
    }

    public void deleteCounterpartyContract(Integer id) {
        if (!counterpartyContractRepo.existsById(id))
            throw new NoSuchRowException("id", id, "Counterparty contract");

        counterpartyContractRepo.deleteById(id);
    }
    // ------------------------------------- Контракт ------------------------------------------

    public void createContract(ContractDto contractDto) {
        Contract contract = contractMapper.mapToContract(contractDto);
        contractRepo.insertContract(contract);
    }

    public void updateContract(Integer contractId, ContractDto contractDto) {
        if (!contractRepo.existsById(contractId))
            throw new  NoSuchRowException("id", contractId, "Contract");
        Integer userId = contractDto.getUserId();
        if (userId != null && !userRepo.existsById(userId))
            throw new NoSuchRowException("id", userId, "User");
        contractDto.setUserId(userId);

        contractRepo.updateContract(contractId, contractDto);
    }

    public void deleteContract(Integer contractId) {
        if (!contractRepo.existsById(contractId))
            throw new  NoSuchRowException("id", contractId, "Contract");

        contractRepo.deleteById(contractId);
    }

    public Page<ContractUserDto> getContractWithUsers(SearchRequest searchRequest) {
        SearchSpecification<Contract> specification = new SearchSpecification<>(searchRequest);
        return contractRepo
                .findAll(specification, PageRequest.of(searchRequest.getPage(), searchRequest.getSize()))
                .map(contractMapper::mapToContractUser);

    }

    public Page<UserDto> getAllUsers(SearchRequest searchRequest) {
        SearchSpecification<User> specification = new SearchSpecification<>(searchRequest);
        return userRepo
                .findAll(specification, PageRequest.of(searchRequest.getPage(), searchRequest.getSize()))
                .map(userMapper::mapToUserDto);
    }

    public void updateUser(Integer userId, UserDto userDto) {
        userDto.setId(userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new NoSuchRowException("id", userId, "user"));
        userMapper.mapToUser(user, userDto);
        userRepo.save(user);
    }

    public void deleteUser(Integer userId) {
        User userToDelete = userRepo.findById(userId).orElseThrow(()-> new NoSuchRowException("id", userId, "user"));

        if (userToDelete.getRoles().stream().anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN)))
            throw new IllegalFieldValueException("Can't delete user with \'ADMIN\' role");

        userRepo.deleteById(userId);
    }
}




















