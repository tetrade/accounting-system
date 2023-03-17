package com.accountingsystem.service;

import com.accountingsystem.dtos.ContractDto;
import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.dtos.mappers.ContractMapper;
import com.accountingsystem.dtos.mappers.CounterpartyContractMapper;
import com.accountingsystem.dtos.pojo.ExcelData;
import com.accountingsystem.repository.ContractRepo;
import com.accountingsystem.repository.CounterpartyContractRepo;
import com.accountingsystem.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private CounterpartyContractMapper counterpartyContractMapper;

    @Mock
    private ContractMapper contractMapper;

    @Mock
    private CounterpartyContractRepo counterpartyContractRepo;

    @Mock
    private ContractRepo contractRepo;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnExcelInfo_whenCalled() {
        CounterpartyContractDto counterpartyContractDto = new CounterpartyContractDto();
        counterpartyContractDto.setName("cc1");

        ContractDto contractDto = new ContractDto();
        contractDto.setName("c1");

        when(counterpartyContractMapper.map(anySet())).thenReturn(
                Stream.of(counterpartyContractDto).collect(Collectors.toSet())
        );

        when(contractMapper.map(anySet())).thenReturn(
                Stream.of(contractDto).collect(Collectors.toSet())
        );

        ExcelData excelData =
                userService.getAllContractsAndCounterpartyContractsBetweenDates("tetrade", LocalDate.MIN, LocalDate.MAX);

        verify(contractRepo).getContractsBetweenDatesByLogin("tetrade", LocalDate.MIN, LocalDate.MAX);
        verify(counterpartyContractRepo).getCounterpartyContractsBetweenDatesByLogin
                ("tetrade", LocalDate.MIN, LocalDate.MAX);

        assertThat(excelData.getContracts())
                .usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsOnly(contractDto);
        assertThat(excelData.getCounterpartyContracts())
                .usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsOnly(counterpartyContractDto);
    }
}
