package com.accountingsystem.service;


import com.accountingsystem.controller.dtos.ContractDto;
import com.accountingsystem.controller.dtos.ContractStageDto;
import com.accountingsystem.controller.dtos.CounterpartyContractDto;
import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.controller.dtos.SignUpRequest;
import com.accountingsystem.controller.dtos.mappers.ContractMapper;
import com.accountingsystem.controller.dtos.mappers.ContractStageMapper;
import com.accountingsystem.controller.dtos.mappers.CounterpartyContractMapper;
import com.accountingsystem.controller.dtos.mappers.CounterpartyOrganizationMapper;
import com.accountingsystem.controller.dtos.mappers.UserMapper;
import com.accountingsystem.entitys.Role;
import com.accountingsystem.entitys.UserLog;
import com.accountingsystem.entitys.enums.ERole;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.excel.dto.CounterpartyContractDtoExcel;
import com.accountingsystem.filters.SearchRequest;
import com.accountingsystem.filters.SearchSpecification;

import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.ContractStage;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.entitys.CounterpartyOrganization;
import com.accountingsystem.entitys.User;
import com.accountingsystem.entitys.enums.EType;


import com.accountingsystem.repository.ContractRepo;
import com.accountingsystem.repository.ContractStageRepo;
import com.accountingsystem.repository.CounterpartyContractRepo;
import com.accountingsystem.repository.CounterpartyOrganizationRepo;
import com.accountingsystem.repository.UserLogRepository;
import com.accountingsystem.repository.UserRepo;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private CounterpartyContractMapper counterpartyContractMapper;

    @Mock
    private ContractMapper contractMapper;

    @Mock
    private CounterpartyContractRepo counterpartyContractRepo;

    @Mock
    private ContractRepo contractRepo;

    @Mock
    private ContractStageRepo contractStageRepo;

    @Mock
    private ContractStageMapper contractStageMapper;

    @Mock
    private CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    @Mock
    private CounterpartyOrganizationMapper counterpartyOrganizationMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserLogRepository userLogRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnSetOfCounterpartyContractDtoInfo_whenCalled() {
        CounterpartyContractDtoExcel should = new CounterpartyContractDtoExcel();
        should.setName("cc1");

        when(contractMapper.mapToCounterpartyContractsDtoExcelSet(anySet())).thenReturn(
                Stream.of(should).collect(Collectors.toSet())
        );

        Set<CounterpartyContractDtoExcel> counterpartyContractDtoExcelSet =
                userService.getCounterpartyContractsBetweenDates("tetrade", LocalDate.MIN, LocalDate.MAX);


        verify(counterpartyContractRepo, times(1)).getCounterpartyContractsBetweenDatesByLogin
                ("tetrade", LocalDate.MIN, LocalDate.MAX);


        assertThat(counterpartyContractDtoExcelSet)
                .usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsOnly(should);
    }

    @Test
    void shouldReturnSetOfContractDtoInfo_whenCalled() {
        ContractDtoExcel should = new ContractDtoExcel();
        should.setName("c1");

        when(contractMapper.mapToContractDtoExcelSet(anySet())).thenReturn(
                Stream.of(should).collect(Collectors.toSet())
        );

        Set<ContractDtoExcel> contractDtoExcelSet = userService.getContractsBetweenDates(
                "tetrade", LocalDate.MIN, LocalDate.MAX
        );

        verify(contractRepo, times(1)).getContractsBetweenDatesByLogin("tetrade", LocalDate.MIN, LocalDate.MAX);

        assertThat(contractDtoExcelSet)
                .usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsOnly(should);
    }

    @Test
    void shouldReturnSetOfContractStage_whenCalled() {
        ContractStageDtoExcel should = new ContractStageDtoExcel();
        should.setName("c1");

        ContractStageDtoExcel should2 = new ContractStageDtoExcel();
        should.setName("c2");

        when(contractStageMapper.mapToContractStageDtoExcelSet(anySet())).thenReturn(
                Stream.of(should, should2).collect(Collectors.toSet())
        );

        Set<ContractStageDtoExcel> contractDtoExcelSet = userService.getContractStagesContractForContractId(
                "tetrade", 1
        );

        verify(contractStageRepo, times(1)).getContractStagesByContractIdAndUserLogin("tetrade", 1);

        assertThat(contractDtoExcelSet)
                .usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsOnly(should, should2);
    }

    @Test
    void shouldReturnPageOfCounterpartyOrganization_whenCalled() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setPage(5);
        searchRequest.setSize(10);

        Page<CounterpartyOrganization> pageMock = mock(Page.class);
        Page<Object> shouldBeReturned = new PageImpl<>(new ArrayList<>());

        when(counterpartyOrganizationRepo.findAll(any(SearchSpecification.class), any(PageRequest.class)))
                .thenReturn(pageMock);
        when(pageMock.map(any())).thenReturn(shouldBeReturned);

        Page<CounterpartyOrganizationDto> p = userService.getCounterpartyOrganizations(searchRequest);

        assertThat(p).isEqualTo(shouldBeReturned);
        verify(counterpartyOrganizationRepo, times(1))
                .findAll(any(SearchSpecification.class), eq(PageRequest.of(searchRequest.getPage(), searchRequest.getSize())));
    }

    @Test
    void shouldReturnPageOfCounterpartyContracts_whenCalled() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setPage(2);
        searchRequest.setSize(5);

        CounterpartyContract cc = new CounterpartyContract();
        cc.setType(EType.DELIVERY);
        cc.setName("test3");
        cc.setAmount(BigDecimal.valueOf(89));
        cc.setPlannedEndDate(LocalDate.now());
        cc.setPlannedStartDate(LocalDate.now().plusDays(12));
        cc.setActualEndDate(LocalDate.of(2002, 9, 9));

        Page<CounterpartyContract> pageMock = mock(Page.class);
        Page<Object> shouldBeReturned = new PageImpl<>(Stream.of(cc).collect(Collectors.toList()));

        when(counterpartyContractRepo.findAll(any(SearchSpecification.class), any(PageRequest.class)))
                .thenReturn(pageMock);
        when(pageMock.map(any())).thenReturn(shouldBeReturned);

        Page<CounterpartyContractDto> p = userService.getCounterpartyContracts(searchRequest);

        assertThat(p).isEqualTo(shouldBeReturned);
        verify(counterpartyContractRepo, times(1))
                .findAll(any(SearchSpecification.class), eq(PageRequest.of(searchRequest.getPage(), searchRequest.getSize())));
    }

    @Test
    void shouldReturnPageOfContractStage_whenCalled() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setPage(2);
        searchRequest.setSize(5);

        ContractStage cs = new ContractStage();
        cs.setName("test1");
        cs.setAmount(BigDecimal.valueOf(123));
        cs.setPlannedMaterialCosts(BigDecimal.valueOf(123.9));
        cs.setPlannedEndDate(LocalDate.now());
        cs.setPlannedStartDate(LocalDate.now().minusWeeks(12));
        cs.setPlannedSalaryExpenses(BigDecimal.valueOf(9));

        ContractStage cs1 = new ContractStage();
        cs1.setName("test2");
        cs1.setAmount(BigDecimal.valueOf(89));
        cs1.setPlannedMaterialCosts(BigDecimal.valueOf(100000.23));
        cs1.setPlannedEndDate(LocalDate.now());
        cs1.setPlannedStartDate(LocalDate.now().plusDays(12));
        cs1.setPlannedSalaryExpenses(BigDecimal.valueOf(9));
        cs1.setActualEndDate(LocalDate.of(2002, 9, 9));

        Page<ContractStage> pageMock = mock(Page.class);
        Page<Object> shouldBeReturned = new PageImpl<>(Stream.of(cs, cs1).collect(Collectors.toList()));

        when(contractStageRepo.findAll(any(SearchSpecification.class), any(PageRequest.class)))
                .thenReturn(pageMock);
        when(pageMock.map(any())).thenReturn(shouldBeReturned);

        Page<ContractStageDto> p = userService.getContractStages(searchRequest);

        assertThat(p).isEqualTo(shouldBeReturned);
        verify(contractStageRepo, times(1))
                .findAll(any(SearchSpecification.class), eq(PageRequest.of(searchRequest.getPage(), searchRequest.getSize())));
    }


    @Test
    void shouldReturnPageOfContracts_whenCalled() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setPage(2);
        searchRequest.setSize(5);

        Contract c = new Contract();
        c.setName("fde9");
        c.setAmount(BigDecimal.valueOf(123.13));
        c.setType(EType.WORKS);
        c.setActualStartDate(LocalDate.of(2002, 2, 10));
        c.setPlannedEndDate(LocalDate.now());
        c.setPlannedStartDate(LocalDate.now().plusDays(2));

        Page<Contract> pageMock = mock(Page.class);
        Page<Object> shouldBeReturned = new PageImpl<>(Stream.of(c).collect(Collectors.toList()));

        when(contractRepo.findAll(any(SearchSpecification.class), any(PageRequest.class)))
                .thenReturn(pageMock);
        when(pageMock.map(any())).thenReturn(shouldBeReturned);

        Page<ContractDto> p = userService.getContracts(searchRequest);

        assertThat(p).isEqualTo(shouldBeReturned);
        verify(contractRepo, times(1))
                .findAll(any(SearchSpecification.class), eq(PageRequest.of(searchRequest.getPage(), searchRequest.getSize())));
    }

    @Test
    void shouldCreateNewUser_whenMethodCalled() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setLogin("new-user");
        signUpRequest.setFullName("test-user");
        signUpRequest.setPassword("qwe123!");

        User user = new User();
        user.setFullName(signUpRequest.getFullName());
        user.setPassword(signUpRequest.getPassword());
        user.setLogin(signUpRequest.getLogin());

        Role r = new Role();
        r.setName(ERole.ROLE_USER);
        r.setId(1);

        user.setRoles(Stream.of(r).collect(Collectors.toSet()));
        user.setTerminationDate(LocalDate.now().plusYears(12));

        when(userMapper.mapToUser(signUpRequest))
                .thenReturn(user);

        userService.createNewUser(signUpRequest);

        verify(userRepo, times(1)).save(user);
    }

    @Test
    void shouldLogUserLogin_whenCalled() {
        String login = "test";
        String ip = "192.168.0.1";

        UserLog userLog = new UserLog();
        userLog.setLogin(login);
        userLog.setIpAddress(ip);
        userLog.setTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 10, 10)));

        userService.logUserLogin(login, ip);


        ArgumentCaptor<UserLog> captor = ArgumentCaptor.forClass(UserLog.class);

        verify(userLogRepository).save(captor.capture());

        assertThat(captor.getValue()).usingRecursiveComparison()
                .ignoringFields("time").isEqualTo(userLog);
    }
}