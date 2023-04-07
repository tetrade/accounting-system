package com.accountingsystem.mappers;

import com.accountingsystem.dtos.*;
import com.accountingsystem.dtos.mappers.*;
import com.accountingsystem.entitys.*;
import com.accountingsystem.enums.ERole;
import com.accountingsystem.enums.EType;
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
public class MappersTest {
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
        counterpartyOrganization.setId(1);
        counterpartyOrganization.setName("Test counterpartyOrganization 1");
        counterpartyOrganization.setInn("123456789012");
        counterpartyOrganization.setAddress("Санкт-Петербург");

        counterpartyContract = new CounterpartyContract();
        counterpartyContract.setId(55);
        counterpartyContract.setAmount(BigDecimal.valueOf(456));
        counterpartyContract.setType(EType.DELIVERY);
        counterpartyContract.setActualStartDate(LocalDate.now());
        counterpartyContract.setActualEndDate(LocalDate.now().plusDays(40));
        counterpartyContract.setActualStartDate(LocalDate.now());
        counterpartyContract.setActualEndDate(LocalDate.now().plusDays(30));
        counterpartyContract.setCounterpartyOrganization(counterpartyOrganization);


        contractStage = new ContractStage();
        contractStage.setName("Test contract stage");
        contractStage.setId(1);

        contract = new Contract();
        contract.setName("Test contract");
        contract.setContractStages(Stream.of(contractStage).collect(Collectors.toSet()));
        contract.setCounterpartyContracts(Stream.of(counterpartyContract).collect(Collectors.toSet()));

        user = new User();
        user.setLogin("tetrade");
        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);
        user.setRoles(Stream.of(role).collect(Collectors.toSet()));
        user.setContracts(Stream.of(contract).collect(Collectors.toSet()));

        contract.setUser(user);
    }

    @Test
    public void shouldReturnSameCounterpartyOrganizationDto_whenMapFromCounterpartyOrganization() {
        CounterpartyOrganizationDto should = new CounterpartyOrganizationDto();
        should.setId(counterpartyOrganization.getId());
        should.setInn(counterpartyOrganization.getInn());
        should.setAddress(counterpartyOrganization.getAddress());
        should.setName(counterpartyOrganization.getName());

        CounterpartyOrganizationDto counterpartyOrganizationDto = counterpartyOrganizationMapper.map(counterpartyOrganization);

       assertThat(counterpartyOrganizationDto).usingRecursiveComparison().isEqualTo(should);
    }

    @Test
    public void shouldReturnSameCounterpartyContractDto_whenMapFromCounterpartyContract() {
       CounterpartyContractDto should = new CounterpartyContractDto();
       should.setName(counterpartyContract.getName());
       should.setId(counterpartyContract.getId());
       should.setType(counterpartyContract.getType());
       should.setAmount(counterpartyContract.getAmount());
       should.setActualStartDate(counterpartyContract.getActualStartDate());
       should.setActualEndDate(counterpartyContract.getActualEndDate());
       should.setPlannedEndDate(counterpartyContract.getPlannedEndDate());
       should.setPlannedStartDate(counterpartyContract.getPlannedStartDate());

       CounterpartyContractDto counterpartyContractDto =
               counterpartyContractMapper.mapToCounterpartyContractDto(counterpartyContract);

        assertThat(counterpartyContractDto).usingRecursiveComparison().isEqualTo(should);
    }

    @Test
    public void shouldReturnSameContractStageDto_whenMapFromContractStage(){
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
    public void shouldReturnSameContractDto_whenMapFromContract() {
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
    public void shouldReturnSameUserDto_whenMapFromUser() {
        UserDto should = new UserDto();
        should.setId(user.getId());
        should.setFullName(user.getFullName());
        should.setPassword(user.getPassword());
        should.setRoles(user.getRoles());
        should.setLogin(user.getLogin());
        should.setDateOfTermination(user.getDateOfTermination());

        should.setContracts(contractMapper.mapToContractDtoSet(user.getContracts()));

        UserDto userDto = userMapper.mapToUserDto(user);

        assertThat(userDto).usingRecursiveComparison().isEqualTo(should);
    }
}
