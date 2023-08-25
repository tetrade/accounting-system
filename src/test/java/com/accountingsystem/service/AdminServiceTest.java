package com.accountingsystem.service;

import com.accountingsystem.advice.exceptions.NoSuchRowException;
import com.accountingsystem.controller.dtos.ContractStageDto;
import com.accountingsystem.controller.dtos.CounterpartyContractDto;
import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.controller.dtos.mappers.*;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
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
    private AdminService adminService;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateCounterpartyOrganization_whenMethodCalled() {
        CounterpartyOrganizationDto counterpartyOrganizationDto = new CounterpartyOrganizationDto();
        counterpartyOrganizationDto.setInn("012345678912");
        counterpartyOrganizationDto.setName("test-name");
        counterpartyOrganizationDto.setAddress("test-address");

        adminService.createCounterpartyOrganization(counterpartyOrganizationDto);

        verify(counterpartyOrganizationRepo, times(1)).insertCounterpartyOrganization(counterpartyOrganizationDto);
    }

    @Test
    void shouldThrowNoSuchRowException_whenUpdateCounterpartyOrganizationCalledWithWrongId() {
        CounterpartyOrganizationDto counterpartyOrganizationDto = new CounterpartyOrganizationDto();
        counterpartyOrganizationDto.setInn("012345678912");
        counterpartyOrganizationDto.setName("test-name");
        counterpartyOrganizationDto.setAddress("test-address");

        int id = 5;

        when(counterpartyOrganizationRepo.existsById(5)).thenReturn(false);
        assertThatThrownBy(() ->
                adminService.updateCounterpartyOrganization(id, counterpartyOrganizationDto)
        ).isInstanceOf(NoSuchRowException.class);

        verify(counterpartyOrganizationRepo, times(1)).existsById(id);
    }

    @Test
    void shouldUpdateCounterpartyOrganization_whenOrganizationExist() {
        CounterpartyOrganizationDto counterpartyOrganizationDto = new CounterpartyOrganizationDto();
        counterpartyOrganizationDto.setInn("012345678912");
        counterpartyOrganizationDto.setName("test-name");
        counterpartyOrganizationDto.setAddress("test-address");

        int id = 5;

        when(counterpartyOrganizationRepo.existsById(5)).thenReturn(true);

        adminService.updateCounterpartyOrganization(id, counterpartyOrganizationDto);

        verify(counterpartyOrganizationRepo, times(1)).existsById(id);
        verify(counterpartyOrganizationRepo, times(1)).updateCounterpartyOrganization(id, counterpartyOrganizationDto);
    }

    @Test
    void shouldThrowNoSuchRowException_whenDeleteCounterpartyOrganizationWithWrongId() {
        int id = 5;
        when(counterpartyOrganizationRepo.existsById(5)).thenReturn(false);
        assertThatThrownBy(() ->
                adminService.deleteCounterpartyOrganization(id)
        ).isInstanceOf(NoSuchRowException.class);

        verify(counterpartyOrganizationRepo, times(1)).existsById(id);
    }

    @Test
    void shouldDeleteCounterpartyOrganization_whenMethodCalledWithRightId() {
        int id = 5;

        when(counterpartyOrganizationRepo.existsById(id)).thenReturn(true);

        adminService.deleteCounterpartyOrganization(id);

        verify(counterpartyOrganizationRepo, times(1)).existsById(id);
        verify(counterpartyOrganizationRepo, times(1)).deleteById(id);
    }

    @Test
    void shouldCreateContractStage_whenCreateStagedWithRightContractId() {
        ContractStageDto contractStageDto = new ContractStageDto();
        contractStageDto.setName("test-stage");
        contractStageDto.setAmount(BigDecimal.valueOf(123));
        contractStageDto.setActualMaterialCosts(BigDecimal.valueOf(45.12));
        contractStageDto.setPlannedMaterialCosts(BigDecimal.TEN);
        contractStageDto.setActualSalaryExpenses(BigDecimal.valueOf(901));
        contractStageDto.setPlannedSalaryExpenses(BigDecimal.valueOf(1901));
        contractStageDto.setActualStartDate(LocalDate.now());
        contractStageDto.setPlannedStartDate(LocalDate.now().minusDays(1));
        contractStageDto.setActualEndDate(null);
        contractStageDto.setPlannedEndDate(LocalDate.now().plusWeeks(5));

        int id = 2;

        when(contractRepo.existsById(id)).thenReturn(true);

        adminService.createContractStage(id, contractStageDto);

        verify(contractRepo, times(1)).existsById(id);
        verify(contractStageRepo, times(1)).insertContractStage(id, contractStageDto);
    }

    @Test
    void shouldThrowNoSuchRowException_whenCreateStageWithWrongContractId() {
        ContractStageDto contractStageDto = new ContractStageDto();
        contractStageDto.setName("test-stage");
        contractStageDto.setAmount(BigDecimal.valueOf(123));
        contractStageDto.setActualMaterialCosts(BigDecimal.valueOf(45.12));
        contractStageDto.setPlannedMaterialCosts(BigDecimal.TEN);
        contractStageDto.setActualSalaryExpenses(BigDecimal.valueOf(901));
        contractStageDto.setPlannedSalaryExpenses(BigDecimal.valueOf(1901));
        contractStageDto.setActualStartDate(LocalDate.now());
        contractStageDto.setPlannedStartDate(LocalDate.now().minusDays(1));
        contractStageDto.setActualEndDate(null);
        contractStageDto.setPlannedEndDate(LocalDate.now().plusWeeks(5));

        int id = 2;

        when(contractRepo.existsById(id)).thenReturn(false);

        assertThatThrownBy(() ->
                adminService.createContractStage(id, contractStageDto)
        ).isInstanceOf(NoSuchRowException.class);

        verify(contractRepo, times(1)).existsById(id);
    }

    @Test
    void shouldThrowNoSuchRowException_whenUpdateStageWithWrongContractStageId() {
        ContractStageDto contractStageDto = new ContractStageDto();
        contractStageDto.setName("test-stage");
        contractStageDto.setAmount(BigDecimal.valueOf(123));
        contractStageDto.setActualMaterialCosts(BigDecimal.valueOf(45.12));
        contractStageDto.setPlannedMaterialCosts(BigDecimal.TEN);
        contractStageDto.setActualSalaryExpenses(BigDecimal.valueOf(901));
        contractStageDto.setPlannedSalaryExpenses(BigDecimal.valueOf(1901));
        contractStageDto.setActualStartDate(LocalDate.now());
        contractStageDto.setPlannedStartDate(LocalDate.now().minusDays(1));
        contractStageDto.setActualEndDate(null);
        contractStageDto.setPlannedEndDate(LocalDate.now().plusWeeks(5));

        int id = 2;

        when(contractStageRepo.existsById(id)).thenReturn(false);

        assertThatThrownBy(() ->
                adminService.updateContractStage(id, contractStageDto)
        ).isInstanceOf(NoSuchRowException.class);

        verify(contractStageRepo, times(1)).existsById(id);
    }

    @Test
    void shouldUpdateContractStage_whenUpdateStageWithRightContractStageId() {
        ContractStageDto contractStageDto = new ContractStageDto();
        contractStageDto.setName("test-stage");
        contractStageDto.setAmount(BigDecimal.valueOf(123));
        contractStageDto.setActualMaterialCosts(BigDecimal.valueOf(45.12));
        contractStageDto.setPlannedMaterialCosts(BigDecimal.TEN);
        contractStageDto.setActualSalaryExpenses(BigDecimal.valueOf(901));
        contractStageDto.setPlannedSalaryExpenses(BigDecimal.valueOf(1901));
        contractStageDto.setActualStartDate(LocalDate.now());
        contractStageDto.setPlannedStartDate(LocalDate.now().minusDays(1));
        contractStageDto.setActualEndDate(null);
        contractStageDto.setPlannedEndDate(LocalDate.now().plusWeeks(5));

        int id = 2;

        when(contractStageRepo.existsById(id)).thenReturn(true);

        adminService.updateContractStage(id, contractStageDto);

        verify(contractStageRepo, times(1)).existsById(id);
        verify(contractStageRepo, times(1)).updateContractStage(id, contractStageDto);
    }

    @Test
    void shouldDeleteContractStage_whenDeleteStageWithRightId() {
        int id = 6;

        when(contractStageRepo.existsById(id)).thenReturn(true);

        adminService.deleteContractStage(id);

        verify(contractStageRepo, times(1)).existsById(id);
        verify(contractStageRepo, times(1)).deleteById(id);
    }

    @Test
    void shouldThrowNoSuchRowException_whenDeleteStageWithRightId() {
        int id = 6;

        when(contractStageRepo.existsById(id)).thenReturn(false);

        assertThatThrownBy(() ->
                adminService.deleteContractStage(id)
        ).isInstanceOf(NoSuchRowException.class);

        verify(contractStageRepo, times(1)).existsById(id);
    }

    @Test
    void shouldCreateCounterpartyContract_whenCreateCContractWithOrganizationAndContractIdExist() {
        int orgId = 5;
        int contractId = 8;
        CounterpartyContractDto counterpartyContractDto = new CounterpartyContractDto();
        counterpartyContractDto.setName("test-contract");
        counterpartyContractDto.setType(EType.WORKS);
        counterpartyContractDto.setAmount(BigDecimal.valueOf(567));
        counterpartyContractDto.setActualStartDate(LocalDate.now());
        counterpartyContractDto.setActualEndDate(LocalDate.now().plusDays(5));
        counterpartyContractDto.setPlannedEndDate(LocalDate.now().plusWeeks(3));
        counterpartyContractDto.setPlannedStartDate(LocalDate.now());
        counterpartyContractDto.setCounterpartyOrganizationId(orgId);

        when(contractRepo.existsById(contractId)).thenReturn(true);
        when(counterpartyOrganizationRepo.existsById(orgId)).thenReturn(true);

        adminService.createCounterpartyContract(contractId, counterpartyContractDto);

        verify(contractRepo, times(1)).existsById(contractId);
        verify(counterpartyOrganizationRepo, times(1)).existsById(orgId);
        verify(counterpartyContractRepo, times(1)).insertCounterpartyContract(contractId, counterpartyContractDto);
    }

    @Test
    void shouldThrowNoSuchRowException_whenCreateCContractWithWrongContractId() {
        int orgId = 5;
        int contractId = 8;
        CounterpartyContractDto counterpartyContractDto = new CounterpartyContractDto();
        counterpartyContractDto.setName("test-contract");
        counterpartyContractDto.setType(EType.WORKS);
        counterpartyContractDto.setAmount(BigDecimal.valueOf(567));
        counterpartyContractDto.setActualStartDate(LocalDate.now());
        counterpartyContractDto.setActualEndDate(LocalDate.now().plusDays(5));
        counterpartyContractDto.setPlannedEndDate(LocalDate.now().plusWeeks(3));
        counterpartyContractDto.setPlannedStartDate(LocalDate.now());
        counterpartyContractDto.setCounterpartyOrganizationId(orgId);

        when(contractRepo.existsById(contractId)).thenReturn(false);

        assertThatThrownBy(() ->
                adminService.createCounterpartyContract(contractId, counterpartyContractDto)
        ).isInstanceOf(NoSuchRowException.class);

        verify(contractRepo, times(1)).existsById(contractId);
        verify(counterpartyOrganizationRepo, times(0)).existsById(orgId);
    }

    @Test
    void shouldThrowNoSuchRowException_whenCreateCContractWithWrongOrganizationId() {
        int orgId = 5;
        int contractId = 8;
        CounterpartyContractDto counterpartyContractDto = new CounterpartyContractDto();
        counterpartyContractDto.setName("test-contract");
        counterpartyContractDto.setType(EType.WORKS);
        counterpartyContractDto.setAmount(BigDecimal.valueOf(567));
        counterpartyContractDto.setActualStartDate(LocalDate.now());
        counterpartyContractDto.setActualEndDate(LocalDate.now().plusDays(5));
        counterpartyContractDto.setPlannedEndDate(LocalDate.now().plusWeeks(3));
        counterpartyContractDto.setPlannedStartDate(LocalDate.now());
        counterpartyContractDto.setCounterpartyOrganizationId(orgId);

        when(contractRepo.existsById(contractId)).thenReturn(true);
        when(counterpartyOrganizationRepo.existsById(orgId)).thenReturn(false);

        assertThatThrownBy(() ->
                adminService.createCounterpartyContract(contractId, counterpartyContractDto)
        ).isInstanceOf(NoSuchRowException.class);

        verify(contractRepo, times(1)).existsById(contractId);
        verify(counterpartyOrganizationRepo, times(1)).existsById(orgId);
    }

    @Test
    void shouldCreateCounterpartyContract_whenUpdateCContractWithOrganizationAndContractIdExist() {
        int orgId = 5;
        int contractId = 8;
        CounterpartyContractDto counterpartyContractDto = new CounterpartyContractDto();
        counterpartyContractDto.setName("test-contract");
        counterpartyContractDto.setType(EType.WORKS);
        counterpartyContractDto.setAmount(BigDecimal.valueOf(567));
        counterpartyContractDto.setActualStartDate(LocalDate.now());
        counterpartyContractDto.setActualEndDate(LocalDate.now().plusDays(5));
        counterpartyContractDto.setPlannedEndDate(LocalDate.now().plusWeeks(3));
        counterpartyContractDto.setPlannedStartDate(LocalDate.now());
        counterpartyContractDto.setCounterpartyOrganizationId(orgId);

        when(counterpartyContractRepo.existsById(contractId)).thenReturn(true);
        when(counterpartyOrganizationRepo.existsById(orgId)).thenReturn(true);

        adminService.updateCounterpartyContract(contractId, counterpartyContractDto);

        verify(counterpartyContractRepo, times(1)).existsById(contractId);
        verify(counterpartyOrganizationRepo, times(1)).existsById(orgId);
        verify(counterpartyContractRepo, times(1)).updateCounterpartyContract(contractId, counterpartyContractDto);
    }

    @Test
    void shouldThrowNoSuchRowException_whenUpdateCContractWithWrongContractId() {
        int orgId = 5;
        int contractId = 8;
        CounterpartyContractDto counterpartyContractDto = new CounterpartyContractDto();
        counterpartyContractDto.setName("test-contract");
        counterpartyContractDto.setType(EType.WORKS);
        counterpartyContractDto.setAmount(BigDecimal.valueOf(567));
        counterpartyContractDto.setActualStartDate(LocalDate.now());
        counterpartyContractDto.setActualEndDate(LocalDate.now().plusDays(5));
        counterpartyContractDto.setPlannedEndDate(LocalDate.now().plusWeeks(3));
        counterpartyContractDto.setPlannedStartDate(LocalDate.now());
        counterpartyContractDto.setCounterpartyOrganizationId(orgId);

        when(counterpartyContractRepo.existsById(contractId)).thenReturn(false);

        assertThatThrownBy(() ->
                adminService.updateCounterpartyContract(contractId, counterpartyContractDto)
        ).isInstanceOf(NoSuchRowException.class);

        verify(counterpartyContractRepo, times(1)).existsById(contractId);
        verify(counterpartyOrganizationRepo, times(0)).existsById(orgId);
    }

    @Test
    void shouldThrowNoSuchRowException_whenUpdateCContractWithWrongOrganizationId() {
        int orgId = 5;
        int contractId = 8;
        CounterpartyContractDto counterpartyContractDto = new CounterpartyContractDto();
        counterpartyContractDto.setName("test-contract");
        counterpartyContractDto.setType(EType.WORKS);
        counterpartyContractDto.setAmount(BigDecimal.valueOf(567));
        counterpartyContractDto.setActualStartDate(LocalDate.now());
        counterpartyContractDto.setActualEndDate(LocalDate.now().plusDays(5));
        counterpartyContractDto.setPlannedEndDate(LocalDate.now().plusWeeks(3));
        counterpartyContractDto.setPlannedStartDate(LocalDate.now());
        counterpartyContractDto.setCounterpartyOrganizationId(orgId);

        when(counterpartyContractRepo.existsById(contractId)).thenReturn(true);
        when(counterpartyOrganizationRepo.existsById(orgId)).thenReturn(false);

        assertThatThrownBy(() ->
                adminService.updateCounterpartyContract(contractId, counterpartyContractDto)
        ).isInstanceOf(NoSuchRowException.class);

        verify(counterpartyContractRepo, times(1)).existsById(contractId);
        verify(counterpartyOrganizationRepo, times(1)).existsById(orgId);
    }
}
