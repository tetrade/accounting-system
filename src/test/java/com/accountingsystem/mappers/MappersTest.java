package com.accountingsystem.mappers;

import com.accountingsystem.advice.exceptions.NoSuchRowException;
import com.accountingsystem.controller.dtos.*;
import com.accountingsystem.controller.dtos.mappers.*;
import com.accountingsystem.entitys.*;
import com.accountingsystem.entitys.enums.ERole;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.excel.dto.ContractDtoExcel;
import com.accountingsystem.excel.dto.ContractStageDtoExcel;
import com.accountingsystem.repository.CounterpartyOrganizationRepo;
import com.accountingsystem.repository.RoleRepo;
import com.accountingsystem.repository.UserRepo;
import org.apache.poi.ss.usermodel.DateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {
        CounterpartyOrganizationMapperImpl.class,
        CounterpartyContractMapperImpl.class,
        ContractStageMapperImpl.class,
        ContractMapperImpl.class,
        UserMapperImpl.class
})
class MappersTest {

    @MockBean
    CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    @MockBean
    UserRepo userRepo;

    @MockBean
    RoleRepo roleRepo;

    @MockBean
    PasswordEncoder passwordEncoder;

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

       should.setCounterpartyOrganization(innerShould);

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

        ContractStageDto should1 = new ContractStageDto();
        should1.setId(contractStage.getId());
        should1.setName(contractStage.getName());
        should1.setAmount(contractStage.getAmount());
        should1.setActualMaterialCosts(contractStage.getActualMaterialCosts());
        should1.setPlannedMaterialCosts(contractStage.getPlannedMaterialCosts());
        should1.setActualSalaryExpenses(contractStage.getActualSalaryExpenses());
        should1.setPlannedSalaryExpenses(contractStage.getPlannedSalaryExpenses());
        should1.setActualStartDate(contractStage.getActualStartDate());
        should1.setPlannedStartDate(contractStage.getPlannedStartDate());
        should1.setActualEndDate(contractStage.getActualEndDate());
        should1.setPlannedEndDate(contractStage.getPlannedEndDate());

        CounterpartyOrganizationDto innerShould = new CounterpartyOrganizationDto();
        innerShould.setId(counterpartyOrganization.getId());
        innerShould.setAddress(counterpartyOrganization.getAddress());
        innerShould.setName(counterpartyOrganization.getName());
        innerShould.setInn(counterpartyOrganization.getInn());

        CounterpartyContractDto should3 = new CounterpartyContractDto();
        should3.setName(counterpartyContract.getName());
        should3.setId(counterpartyContract.getId());
        should3.setType(counterpartyContract.getType());
        should3.setAmount(counterpartyContract.getAmount());
        should3.setActualStartDate(counterpartyContract.getActualStartDate());
        should3.setActualEndDate(counterpartyContract.getActualEndDate());
        should3.setPlannedEndDate(counterpartyContract.getPlannedEndDate());
        should3.setPlannedStartDate(counterpartyContract.getPlannedStartDate());
        should3.setCounterpartyOrganization(innerShould);

        should.setContractStages(Stream.of(should1).collect(Collectors.toSet()));
        should.setCounterpartyContracts(Stream.of(should3).collect(Collectors.toSet()));

        ContractDto contractDto = contractMapper.mapToContractDto(contract);
        Set<ContractDto> contractDtoSet1 = contractMapper.mapToContractDtoSet(Stream.of(contract).collect(Collectors.toSet()));
        Set<ContractDto> contractDtoSet2 = contractMapper.mapToContractDtoSet(Stream.of(contract).collect(Collectors.toList()));

        assertThat(contractDto).usingRecursiveComparison().isEqualTo(should);
        assertThat(contractDtoSet1).usingRecursiveFieldByFieldElementComparatorIgnoringFields().containsOnly(should);
        assertThat(contractDtoSet1).isEqualTo(contractDtoSet2);
    }


    @Test
    void shouldReturnContract_whenMapContactDtoToContacts() {
        ContractDto contactDto = new ContractDto();
        contactDto.setId(contract.getId());
        contactDto.setName(contract.getName());
        contactDto.setAmount(contract.getAmount());
        contactDto.setType(contract.getType());
        contactDto.setActualStartDate(contract.getActualStartDate());
        contactDto.setPlannedStartDate(contract.getPlannedStartDate());
        contactDto.setActualEndDate(contract.getActualEndDate());
        contactDto.setPlannedEndDate(contract.getPlannedEndDate());

        ContractStageDto contactsStageDto = new ContractStageDto();
        contactsStageDto.setId(contractStage.getId());
        contactsStageDto.setName(contractStage.getName());
        contactsStageDto.setAmount(contractStage.getAmount());
        contactsStageDto.setActualMaterialCosts(contractStage.getActualMaterialCosts());
        contactsStageDto.setPlannedMaterialCosts(contractStage.getPlannedMaterialCosts());
        contactsStageDto.setActualSalaryExpenses(contractStage.getActualSalaryExpenses());
        contactsStageDto.setPlannedSalaryExpenses(contractStage.getPlannedSalaryExpenses());
        contactsStageDto.setActualStartDate(contractStage.getActualStartDate());
        contactsStageDto.setPlannedStartDate(contractStage.getPlannedStartDate());
        contactsStageDto.setActualEndDate(contractStage.getActualEndDate());
        contactsStageDto.setPlannedEndDate(contractStage.getPlannedEndDate());

        contactDto.setContractStages(Stream.of(contactsStageDto).collect(Collectors.toSet()));

        Contract mappedContract = contractMapper.mapToContract(contactDto);
        assertThat(mappedContract).usingRecursiveComparison().ignoringFields("contractStages", "counterpartyContracts", "user")
                .isEqualTo(contract);

        assertThat(mappedContract.getContractStages()).extracting("contract").containsOnly(mappedContract);
    }

    @Test
    void shouldReturnContractStageDtoExcel_whenMappedContractStageToContractsStageDtoExcel() {
        ContractStageDto contractStageDto = new ContractStageDto();
        contractStageDto.setId(contractStage.getId());
        contractStageDto.setName(contractStage.getName());
        contractStageDto.setAmount(contractStage.getAmount());
        contractStageDto.setActualStartDate(contractStage.getActualStartDate());
        contractStageDto.setPlannedStartDate(contractStage.getPlannedStartDate());
        contractStageDto.setActualEndDate(contractStage.getActualEndDate());
        contractStageDto.setPlannedEndDate(contractStage.getPlannedEndDate());
        contractStageDto.setActualMaterialCosts(contractStage.getActualMaterialCosts());
        contractStageDto.setPlannedMaterialCosts(contractStage.getPlannedMaterialCosts());
        contractStageDto.setActualSalaryExpenses(contractStage.getActualSalaryExpenses());
        contractStageDto.setPlannedSalaryExpenses(contractStage.getPlannedSalaryExpenses());


        ContractStageDtoExcel should = new ContractStageDtoExcel();
        should.setName(contractStage.getName());
        should.setAmount(contractStage.getAmount().doubleValue());
        should.setActualStartDate(DateUtil.getExcelDate(contractStage.getActualStartDate()));
        should.setPlannedStartDate(DateUtil.getExcelDate(contractStage.getPlannedStartDate()));
        should.setActualEndDate(DateUtil.getExcelDate(contractStage.getActualEndDate()));
        should.setPlannedEndDate(DateUtil.getExcelDate(contractStage.getPlannedEndDate()));
        should.setActualMaterialCosts(null);
        should.setPlannedMaterialCosts(contractStage.getPlannedMaterialCosts().doubleValue());
        should.setActualSalaryExpenses(contractStage.getActualSalaryExpenses().doubleValue());
        should.setPlannedSalaryExpenses(contractStage.getPlannedSalaryExpenses().doubleValue());

        assertThat(contractStageMapper.mapToContractStageDtoExcel(contractStage))
                .usingRecursiveComparison().isEqualTo(should);

    }

    @Test
    void shouldCallFindByIdAndReturnCounterpartyOrganization_whenMappedIdToCounterpartyOrganization() {

        when(counterpartyOrganizationRepo.findById(any()))
                .thenReturn(Optional.of(counterpartyOrganization));

        assertThat(counterpartyOrganizationMapper.mapIdToCounterpartyOrganization(5))
                .isEqualTo(counterpartyOrganization);

    }

    @Test
    void shouldThrowExp_whenMappedIdToCounterpartyOrganizationWithWrongId() {

        when(counterpartyOrganizationRepo.findById(1))
                .thenThrow(new NoSuchRowException("id", 1, "counterparty organization"));

        assertThatThrownBy(() -> counterpartyOrganizationMapper.mapIdToCounterpartyOrganization(5))
                .isInstanceOf(NoSuchRowException.class);

    }


    @Test
    void shouldReturnUser_whenMappedIdToUserWithExistedUser() {
        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        assertThat(userMapper.mapIdToUser(1))
                .isEqualTo(user);
    }

    @Test
    void shouldThrowExp_whenMappedIdToUserWithWrongId() {

        when(userRepo.findById(1)).thenThrow(new NoSuchRowException("id", 1, "user"));

        assertThatThrownBy(() -> userMapper.mapIdToUser(1))
                .isInstanceOf(NoSuchRowException.class);

    }

    @Test
    void shouldReturnSameUserDto_whenMapFromUser() {
        UserDto should = new UserDto();
        should.setId(user.getId());
        should.setFullName(user.getFullName());
        should.setNewPassword(user.getPassword());
        should.setLogin(user.getLogin());
        should.setIsAdmin(true);

        UserDto userDto = userMapper.mapToUserDto(user);

        assertThat(userDto).usingRecursiveComparison()
                .ignoringFields("newPassword").isEqualTo(should);
    }
}
