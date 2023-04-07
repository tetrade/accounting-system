package com.accountingsystem.service;

import com.accountingsystem.dtos.mappers.ContractMapper;
import com.accountingsystem.dtos.mappers.CounterpartyContractMapper;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import com.accountingsystem.repository.ContractRepo;
import com.accountingsystem.repository.CounterpartyContractRepo;
import com.accountingsystem.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.Set;
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
    public void shouldReturnSetOfCounterpartyContractDtoInfo_whenCalled() {
        CounterpartyContractDtoExcel should = new CounterpartyContractDtoExcel();
        should.setName("cc1");

        when(counterpartyContractMapper.mapToCounterpartyContractsDtoExcelSet(anySet())).thenReturn(
                Stream.of(should).collect(Collectors.toSet())
        );

        Set<CounterpartyContractDtoExcel> counterpartyContractDtoExcelSet =
                userService.getCounterpartyContractsBetweenDates("tetrade", LocalDate.MIN, LocalDate.MAX);


        verify(counterpartyContractRepo).getCounterpartyContractsBetweenDatesByLogin
                ("tetrade", LocalDate.MIN, LocalDate.MAX);


        assertThat(counterpartyContractDtoExcelSet)
                .usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsOnly(should);
    }

    @Test
    public void shouldReturnSetOfContractDtoInfo_whenCalled() {
        ContractDtoExcel should = new ContractDtoExcel();
        should.setName("c1");

        when(contractMapper.mapToContractDtoExcelSet(anySet())).thenReturn(
                Stream.of(should).collect(Collectors.toSet())
        );

        Set<ContractDtoExcel> contractDtoExcelSet = userService.getContractsBetweenDates(
                "tetrade", LocalDate.MIN, LocalDate.MAX
        );

        verify(contractRepo).getContractsBetweenDatesByLogin("tetrade", LocalDate.MIN, LocalDate.MAX);

        assertThat(contractDtoExcelSet)
                .usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsOnly(should);
    }
}
