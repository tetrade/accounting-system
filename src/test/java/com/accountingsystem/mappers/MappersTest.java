package com.accountingsystem.mappers;

import com.accountingsystem.controller.dtos.*;
import com.accountingsystem.controller.dtos.mappers.*;
import com.accountingsystem.entitys.*;
import com.accountingsystem.entitys.enums.ERole;
import com.accountingsystem.entitys.enums.EType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {
        CounterpartyOrganizationMapperImpl.class,
        CounterpartyContractMapperImpl.class,
        ContractStageMapperImpl.class,
        ContractMapperImpl.class,
        UserMapperImpl.class
})

class MappersTest {

    @Autowired
    private CounterpartyOrganizationMapper counterpartyOrganizationMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private CounterpartyContractMapper counterpartyContractMapper;

    @Autowired
    private ContractStageMapper contractStageMapper;

    @Autowired
    private UserMapper userMapper;

    private static CounterpartyOrganization counterpartyOrganization;
    private static CounterpartyContract counterpartyContract;
    private static ContractStage contractStage;
    private static Contract contract;
    private static User user;

    @BeforeAll
    static void setUp() {
        counterpartyOrganization = new CounterpartyOrganization();
        counterpartyOrganization.setId(5);
        counterpartyOrganization.setName("Test counterpartyOrganization 1");
        counterpartyOrganization.setInn("123456789012");
        counterpartyOrganization.setAddress("Санкт-Петербург");

        counterpartyContract = new CounterpartyContract();
        counterpartyContract.setName("8912da");
        counterpartyContract.setId(55);
        counterpartyContract.setAmount(BigDecimal.valueOf(456));
        counterpartyContract.setType(EType.DELIVERY);
        counterpartyContract.setPlannedStartDate(LocalDate.of(1999, 7, 20));
        counterpartyContract.setActualStartDate(LocalDate.now());
        counterpartyContract.setActualEndDate(LocalDate.now().plusDays(40));
        counterpartyContract.setActualStartDate(LocalDate.now());
        counterpartyContract.setActualEndDate(LocalDate.now().plusDays(30));
        counterpartyContract.setCounterpartyOrganization(counterpartyOrganization);


        contractStage = new ContractStage();
        contractStage.setName("Test contract stage");
        contractStage.setId(1);
        contractStage.setAmount(BigDecimal.TEN);
        contractStage.setPlannedEndDate(LocalDate.now());
        contractStage.setPlannedStartDate(LocalDate.now().plusWeeks(2));
        contractStage.setActualStartDate(LocalDate.now().minusYears(12));
        contractStage.setActualEndDate(LocalDate.of(2002, 10, 10));
        contractStage.setPlannedSalaryExpenses(BigDecimal.TEN.pow(2));
        contractStage.setActualSalaryExpenses(BigDecimal.valueOf(651));
        contractStage.setPlannedMaterialCosts(BigDecimal.ZERO);

        contract = new Contract();
        contract.setId(10);
        contract.setName("Test contract");
        contract.setAmount(BigDecimal.valueOf(999));
        contract.setActualStartDate(LocalDate.now());
        contract.setActualStartDate(LocalDate.now().plusWeeks(4));
        contract.setPlannedStartDate(LocalDate.of(2022, 10, 4));
        contract.setActualEndDate(LocalDate.of(2022, 12, 5));
        contract.setContractStages(Stream.of(contractStage).collect(Collectors.toSet()));
        contract.setCounterpartyContracts(Stream.of(counterpartyContract).collect(Collectors.toSet()));

        user = new User();
        user.setId(29);
        user.setLogin("tetrade");
        user.setPassword("qwe");
        user.setFullName("Test testing");
        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);
        user.setRoles(Stream.of(role).collect(Collectors.toSet()));
        user.setContracts(Stream.of(contract).collect(Collectors.toSet()));

        contract.setUser(user);
    }

    @Test
    void shouldReturnSameCounterpartyOrganizationDto_whenMapFromCounterpartyOrganization() {
        CounterpartyOrganizationDto should = new CounterpartyOrganizationDto();
        should.setId(counterpartyOrganization.getId());
        should.setInn(counterpartyOrganization.getInn());
        should.setAddress(counterpartyOrganization.getAddress());
        should.setName(counterpartyOrganization.getName());

        CounterpartyOrganizationDto counterpartyOrganizationDto = counterpartyOrganizationMapper.map(counterpartyOrganization);

       assertThat(counterpartyOrganizationDto).usingRecursiveComparison().isEqualTo(should);
    }

    @Test
    void shouldReturnSameCounterpartyContractDto_whenMapFromCounterpartyContract() {
       CounterpartyContractDto should = new CounterpartyContractDto();
       should.setName(counterpartyContract.getName());
       should.setId(counterpartyContract.getId());
       should.setType(counterpartyContract.getType());
       should.setAmount(counterpartyContract.getAmount());
       should.setActualStartDate(counterpartyContract.getActualStartDate());
       should.setActualEndDate(counterpartyContract.getActualEndDate());
       should.setPlannedEndDate(counterpartyContract.getPlannedEndDate());
       should.setPlannedStartDate(counterpartyContract.getPlannedStartDate());

       CounterpartyOrganizationDto innerShould = new CounterpartyOrganizationDto();
       innerShould.setId(counterpartyOrganization.getId());
       innerShould.setAddress(counterpartyOrganization.getAddress());
       innerShould.setName(counterpartyOrganization.getName());
       innerShould.setInn(counterpartyOrganization.getInn());

       should.setCounterpartyOrganizationDto(innerShould);

       CounterpartyContractDto counterpartyContractDto =
               counterpartyContractMapper.mapToCounterpartyContractDto(counterpartyContract);

        assertThat(counterpartyContractDto).usingRecursiveComparison().isEqualTo(should);
    }

    @Test
    void shouldReturnSameContractStageDto_whenMapFromContractStage(){
        ContractStageDto should = new ContractStageDto();
        should.setId(contractStage.getId());
        should.setName(contractStage.getName());
        should.setAmount(contractStage.getAmount());
        should.setActualMaterialCosts(contractStage.getActualMaterialCosts());
        should.setPlannedMaterialCosts(contractStage.getPlannedMaterialCosts());
        should.setActualSalaryExpenses(contractStage.getActualSalaryExpenses());
        should.setPlannedSalaryExpenses(contractStage.getPlannedSalaryExpenses());
        should.setActualStartDate(contractStage.getActualStartDate());
        should.setPlannedStartDate(contractStage.getPlannedStartDate());
        should.setActualEndDate(contractStage.getActualEndDate());
        should.setPlannedEndDate(contractStage.getPlannedEndDate());

        ContractStageDto contractStageDto = contractStageMapper.mapToContractStageDto(contractStage);

        assertThat(contractStageDto).usingRecursiveComparison().isEqualTo(should);
    }

    @Test
    void shouldReturnSameContractDto_whenMapFromContract() {
        ContractDto should = new ContractDto();
        should.setId(contract.getId());
        should.setName(contract.getName());
        should.setAmount(contract.getAmount());
        should.setType(contract.getType());
        should.setActualStartDate(contract.getActualStartDate());
        should.setPlannedStartDate(contract.getPlannedStartDate());
        should.setActualEndDate(contract.getActualEndDate());
        should.setPlannedEndDate(contract.getPlannedEndDate());

        ContractDto contractDto = contractMapper.mapToContractDto(contract);
        Set<ContractDto> contractDtoSet1 = contractMapper.mapToContractDtoSet(Stream.of(contract).collect(Collectors.toSet()));
        Set<ContractDto> contractDtoSet2 = contractMapper.mapToContractDtoSet(Stream.of(contract).collect(Collectors.toList()));


        assertThat(contractDto).usingRecursiveComparison().isEqualTo(should);
        assertThat(contractDtoSet1).usingRecursiveFieldByFieldElementComparatorIgnoringFields().containsOnly(should);
        assertThat(contractDtoSet1).isEqualTo(contractDtoSet2);
    }

    @Test
    void shouldReturnSameUserDto_whenMapFromUser() {
        UserDto should = new UserDto();
        should.setId(user.getId());
        should.setFullName(user.getFullName());
        should.setPassword(user.getPassword());
        should.setLogin(user.getLogin());

        UserDto userDto = userMapper.mapToUserDto(user);

        assertThat(userDto).usingRecursiveComparison().isEqualTo(should);
    }
}
