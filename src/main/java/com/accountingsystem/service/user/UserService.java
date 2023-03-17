package com.accountingsystem.service.user;

import com.accountingsystem.dtos.ContractDto;
import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.dtos.mappers.ContractMapper;
import com.accountingsystem.dtos.mappers.CounterpartyContractMapper;
import com.accountingsystem.dtos.pojo.ExcelData;
import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.repository.ContractRepo;
import com.accountingsystem.repository.CounterpartyContractRepo;
import com.accountingsystem.repository.CounterpartyOrganizationRepo;
import com.accountingsystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class UserService {

    private final ContractRepo contractRepo;

    private final CounterpartyContractRepo counterpartyContractRepo;

    private final CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    private final UserRepo userRepo;

    private final ContractMapper contractMapper;

    private final CounterpartyContractMapper counterpartyContractMapper;

    @Autowired
    public UserService(ContractRepo contractRepo, CounterpartyContractRepo counterpartyContractRepo,
                       CounterpartyOrganizationRepo counterpartyOrganizationRepo, UserRepo userRepo,
                       CounterpartyContractMapper counterpartyContractMapper, ContractMapper contractMapper
    ) {
        this.contractRepo = contractRepo;
        this.counterpartyOrganizationRepo = counterpartyOrganizationRepo;
        this.counterpartyContractRepo = counterpartyContractRepo;
        this.userRepo = userRepo;
        this.contractMapper = contractMapper;
        this.counterpartyContractMapper = counterpartyContractMapper;
    }

    public ExcelData getAllContractsAndCounterpartyContractsBetweenDates(
            String login, LocalDate startDate, LocalDate endDate
    ) {
        Set<Contract> contractsBetweenDates = contractRepo.getContractsBetweenDatesByLogin(login, startDate, endDate);
        Set<ContractDto> contractsDtoBetweenDates = contractMapper
                .map(contractsBetweenDates);

        Set<CounterpartyContract> counterpartyContractsBetweenDates =
                counterpartyContractRepo.getCounterpartyContractsBetweenDatesByLogin(login, startDate, endDate);
        Set<CounterpartyContractDto> counterpartyContractsDtoBetweenDates = counterpartyContractMapper
                .map(counterpartyContractsBetweenDates);

        return ExcelData.builder()
                .counterpartyContracts(counterpartyContractsDtoBetweenDates)
                .contracts(contractsDtoBetweenDates)
                .build();
    }

}
