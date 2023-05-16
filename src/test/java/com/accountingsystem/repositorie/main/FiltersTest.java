package com.accountingsystem.repositorie.main;

import com.accountingsystem.entitys.*;
import com.accountingsystem.entitys.enums.ERole;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.filters.*;
import com.accountingsystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class FiltersTest {

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private UserLogRepository userLogRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ContractRepo contractRepo;

    @Autowired
    private ContractStageRepo contractStageRepo;

    @Autowired
    private CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    @Autowired
    private CounterpartyContractRepo counterpartyContractRepo;

    private User user1;
    private User user2;

    private Contract contract1;
    private Contract contract2;
    private Contract contract3;

    private CounterpartyContract counterpartyContract1;
    private CounterpartyContract counterpartyContract2;
    private CounterpartyContract counterpartyContract3;
    private CounterpartyContract counterpartyContract4;
    private CounterpartyContract counterpartyContract5;

    private ContractStage contractStage1;
    private ContractStage contractStage2;

    private CounterpartyOrganization counterpartyOrganization1;
    private CounterpartyOrganization counterpartyOrganization2;
    @Autowired
    private RoleRepo roleRepo;

    @BeforeEach
    public void setUp() {
        Role role = roleRepo.findByName(ERole.ROLE_USER).orElse(null);

        user1 = new User();
        user1.setLogin("user1");
        user1.setFullName("user1");
        user1.setPassword("p");
        user1.setRoles(Stream.of(role).collect(Collectors.toSet()));

        user2 = new User();
        user2.setLogin("user2");
        user2.setFullName("user2");
        user2.setPassword("p");
        user2.setRoles(Stream.of(role).collect(Collectors.toSet()));

        contract1 = new Contract();
        contract1.setAmount(BigDecimal.valueOf(1351));
        contract1.setPlannedEndDate(LocalDate.of(2002, 1, 1));
        contract1.setPlannedStartDate(LocalDate.of(2002, 1, 1).plusWeeks(5));
        contract1.setActualStartDate(LocalDate.now());
        contract1.setActualEndDate(LocalDate.now().plusWeeks(2));
        contract1.setType(EType.WORKS);
        contract1.setName("contract1");

        contract2 = new Contract();
        contract2.setAmount(BigDecimal.valueOf(752782));
        contract2.setPlannedEndDate(LocalDate.of(2001, 4, 15));
        contract2.setPlannedStartDate(LocalDate.of(2001, 4, 15).plusYears(2));
        contract2.setActualStartDate(LocalDate.now().minusYears(2));
        contract2.setActualEndDate(LocalDate.now().plusWeeks(5));
        contract2.setType(EType.DELIVERY);
        contract2.setName("contract2");

        contract3 = new Contract();
        contract3.setAmount(BigDecimal.valueOf(713));
        contract3.setPlannedEndDate(LocalDate.of(2000, 2, 2));
        contract3.setPlannedStartDate(LocalDate.of(2000, 2, 2).plusYears(6));
        contract3.setActualStartDate(LocalDate.of(2020, 9, 13));
        contract3.setActualEndDate(LocalDate.of(2020, 9, 13).plusDays(2));
        contract3.setType(EType.DELIVERY);
        contract3.setName("contract3");

        user1.addContract(contract1);
        user1.addContract(contract2);
        user2.addContract(contract3);

        counterpartyOrganization1 = new CounterpartyOrganization();
        counterpartyOrganization1.setName("counterpartyOrganization1");
        counterpartyOrganization1.setAddress("123adsf");
        counterpartyOrganization1.setInn("123456789012");

        counterpartyOrganization2 = new CounterpartyOrganization();
        counterpartyOrganization2.setName("counterpartyOrganization2");
        counterpartyOrganization2.setAddress("123fsadsf");
        counterpartyOrganization2.setInn("123456789013");

        counterpartyContract1 = new CounterpartyContract();
        counterpartyContract1.setAmount(BigDecimal.ZERO);
        counterpartyContract1.setType(EType.WORKS);
        counterpartyContract1.setPlannedStartDate(LocalDate.of(2020, 2, 8));
        counterpartyContract1.setPlannedEndDate(LocalDate.now().minusYears(1));
        counterpartyContract1.setActualEndDate(LocalDate.of(2023, 1, 15));
        counterpartyContract1.setName("counterpartyContract1");
        counterpartyContract1.setCounterpartyOrganization(counterpartyOrganization1);
        contract1.addCounterpartyContract(counterpartyContract1);

        counterpartyContract2 = new CounterpartyContract();
        counterpartyContract2.setAmount(BigDecimal.valueOf(611));
        counterpartyContract2.setType(EType.PURCHASE);
        counterpartyContract2.setPlannedStartDate(LocalDate.now());
        counterpartyContract2.setPlannedEndDate(LocalDate.now().plusWeeks(20));
        counterpartyContract2.setActualEndDate(LocalDate.of(2022, 5, 5));
        counterpartyContract2.setActualStartDate(LocalDate.of(2022, 5, 5).minusWeeks(21));
        counterpartyContract2.setName("counterpartyContract2");
        counterpartyContract2.setCounterpartyOrganization(counterpartyOrganization1);
        contract1.addCounterpartyContract(counterpartyContract2);

        counterpartyContract3 = new CounterpartyContract();
        counterpartyContract3.setAmount(BigDecimal.valueOf(1000));
        counterpartyContract3.setType(EType.WORKS);
        counterpartyContract3.setPlannedStartDate(LocalDate.of(2020, 11, 28));
        counterpartyContract3.setPlannedEndDate(LocalDate.of(2020, 11, 28).plusWeeks(4).plusDays(2));
        counterpartyContract3.setActualEndDate(LocalDate.of(2023, 1, 15));
        counterpartyContract3.setName("counterpartyContract3");
        counterpartyContract3.setCounterpartyOrganization(counterpartyOrganization2);
        contract1.addCounterpartyContract(counterpartyContract3);

        counterpartyContract4 = new CounterpartyContract();
        counterpartyContract4.setAmount(BigDecimal.valueOf(1400));
        counterpartyContract4.setType(EType.DELIVERY);
        counterpartyContract4.setPlannedStartDate(LocalDate.of(2019, 4, 12));
        counterpartyContract4.setPlannedEndDate(LocalDate.of(2019, 4, 12).plusWeeks(2));
        counterpartyContract4.setActualStartDate(LocalDate.of(2019, 5, 6));
        counterpartyContract4.setActualEndDate(LocalDate.of(2019, 5, 20));
        counterpartyContract4.setName("counterpartyContract4");
        counterpartyContract4.setCounterpartyOrganization(counterpartyOrganization1);
        contract2.addCounterpartyContract(counterpartyContract4);

        counterpartyContract5 = new CounterpartyContract();
        counterpartyContract5.setAmount(BigDecimal.valueOf(879.31));
        counterpartyContract5.setType(EType.WORKS);
        counterpartyContract5.setPlannedStartDate(LocalDate.of(2018, 3, 2));
        counterpartyContract5.setPlannedEndDate(LocalDate.of(2018, 3, 2).plusWeeks(6));
        counterpartyContract5.setActualStartDate(LocalDate.now());
        counterpartyContract5.setName("counterpartyContract5");
        counterpartyContract5.setCounterpartyOrganization(counterpartyOrganization2);
        contract3.addCounterpartyContract(counterpartyContract5);

        contractStage1 = new ContractStage();
        contractStage1.setAmount(BigDecimal.valueOf(78));
        contractStage1.setPlannedStartDate(LocalDate.of(2010, 7, 23));
        contractStage1.setPlannedEndDate(LocalDate.of(2010, 7, 23).plusWeeks(2).plusDays(2));
        contractStage1.setPlannedMaterialCosts(BigDecimal.valueOf(67132));
        contractStage1.setPlannedSalaryExpenses(BigDecimal.valueOf(7521));
        contractStage1.setName("contractStage1");
        contract1.addContractStage(contractStage1);

        contractStage2 = new ContractStage();
        contractStage2.setAmount(BigDecimal.valueOf(1293));
        contractStage2.setPlannedStartDate(LocalDate.now());
        contractStage2.setPlannedEndDate(LocalDate.now().plusDays(222));
        contractStage2.setPlannedMaterialCosts(BigDecimal.valueOf(120.31));
        contractStage2.setPlannedSalaryExpenses(BigDecimal.valueOf(35134));
        contractStage2.setActualMaterialCosts(BigDecimal.valueOf(41));
        contractStage2.setActualSalaryExpenses(BigDecimal.valueOf(13513.31));
        contractStage2.setName("contractStage2");
        contract2.addContractStage(contractStage2);

        userRepo.save(user1);
        userRepo.save(user2);
    }


    @Test
    void shouldReturnAllContracts_whenDontHaveFilters() {

        // when
        SearchSpecification<Contract> specification = new SearchSpecification<>(new SearchRequest());
        List<Contract> contracts = contractRepo.findAll(specification);

        assertThat(contracts).usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(contract1, contract2, contract3);
    }

    @Test
    void shouldReturnContractsOfUser1_whenFiltersContainsUserFilter() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addUserLoginFilter(user1.getLogin());

        // when
        SearchSpecification<Contract> specification = new SearchSpecification<>(searchRequest);
        List<Contract> contracts = contractRepo.findAll(specification);

        assertThat(contracts).usingRecursiveFieldByFieldElementComparator()
                .containsAll(user1.getContracts());
    }

    @Test
    void shouldReturnContractOfUser1WithAmountGreater_whenFilterContainsUserAndAmountFilter() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addUserLoginFilter(user1.getLogin());

        searchRequest.addFilter(
                new FilterRequest(EPublicKey.AMOUNT, ETargetEntity.CONTRACT, EOperator.GREATER, contract1.getAmount().toString())
        );

        SearchSpecification<Contract> specification = new SearchSpecification<>(searchRequest);
        List<Contract> contracts = contractRepo.findAll(specification);

        assertThat(contracts).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(contract2);
    }

    @Test
    void shouldReturnContractWithSpecificCounterpartyContractType_whenFilterContainsCounterpartyContractFilter() {
        SearchRequest searchRequest = new SearchRequest();

        searchRequest.addFilter(
                new FilterRequest(EPublicKey.TYPE, ETargetEntity.COUNTERPARTY_CONTRACT,
                        EOperator.EQUAL, counterpartyContract1.getType().getType())
        );

        SearchSpecification<Contract> specification = new SearchSpecification<>(searchRequest);
        List<Contract> contracts = contractRepo.findAll(specification);


        assertThat(contracts).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(contract1, contract3);
    }

    @Test
    void shouldReturnContractWithSpecificContractStagePlannedStartDateAndCounterpartyContractNameLike_whenFilterContainsCounterpartyContractAndContractStageFilter() {
        SearchRequest searchRequest = new SearchRequest();

        searchRequest.addFilter(
                new FilterRequest(EPublicKey.PLANNED_START_DATE, ETargetEntity.COUNTERPARTY_CONTRACT,
                        EOperator.LESS, "09-02-2020")
        );

        searchRequest.addFilter(
                new FilterRequest(EPublicKey.NAME, ETargetEntity.CONTRACT_STAGE,
                        EOperator.LIKE, contractStage1.getName())
        );

        SearchSpecification<Contract> specification = new SearchSpecification<>(searchRequest);
        List<Contract> contracts = contractRepo.findAll(specification);

        assertThat(contracts).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(contract1);
    }

    @Test
    void shouldReturnContractThatHaveCounterpartyContractWithSpecificCounterpartyOrgInn_whenContractWithCounterpartyContractFilter(){
        SearchRequest searchRequest = new SearchRequest();

        searchRequest.addFilter(
                new FilterRequest(EPublicKey.INN, ETargetEntity.COUNTERPARTY_ORGANIZATION,
                        EOperator.EQUAL, counterpartyOrganization2.getInn())
        );

        SearchSpecification<Contract> specification = new SearchSpecification<>(searchRequest);
        List<Contract> contracts = contractRepo.findAll(specification);

        assertThat(contracts).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(contract1, contract3);
    }

    @Test
    void shouldReturnCounterpartyContractForContract_whenCounterpartyContractWithFilter() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contract1.getId());

        SearchSpecification<CounterpartyContract> specification = new SearchSpecification<>(searchRequest);
        List<CounterpartyContract> counterpartyContracts = counterpartyContractRepo.findAll(specification);

        assertThat(counterpartyContracts).usingRecursiveFieldByFieldElementComparator()
                .containsOnlyOnceElementsOf(contract1.getCounterpartyContracts());
    }

    @Test
    void shouldReturnCounterpartyContractsThatAmountGreaterAndActualEndDateLess_whenCounterpartyContractWithThisFilters() {
        SearchRequest searchRequest = new SearchRequest();

        searchRequest.addFilter(
                new FilterRequest(EPublicKey.AMOUNT, ETargetEntity.COUNTERPARTY_CONTRACT,
                        EOperator.GREATER, "999")
        );

        searchRequest.addFilter(
                new FilterRequest(EPublicKey.ACTUAL_END_DATE, ETargetEntity.COUNTERPARTY_CONTRACT,
                        EOperator.LESS, "01-01-2020")
        );

        SearchSpecification<CounterpartyContract> specification = new SearchSpecification<>(searchRequest);
        List<CounterpartyContract> counterpartyContracts = counterpartyContractRepo.findAll(specification);

        assertThat(counterpartyContracts).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(counterpartyContract4);
    }

    @Test
    void shouldReturnCounterpartyContractsWithSpecificCounterpartyOrganization() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addEntityIdFilter(ETargetEntity.COUNTERPARTY_ORGANIZATION, counterpartyOrganization1.getId());

        SearchSpecification<CounterpartyContract> specification = new SearchSpecification<>(searchRequest);
        List<CounterpartyContract> counterpartyContracts = counterpartyContractRepo.findAll(specification);

        assertThat(counterpartyContracts).usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(counterpartyOrganization1.getCounterpartyContracts());
    }

    @Test
    void shouldReturnContractStagesForContract_whenContractStageWithContractFilter() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addEntityIdFilter(ETargetEntity.CONTRACT, contract1.getId());

        SearchSpecification<ContractStage> specification = new SearchSpecification<>(searchRequest);
        List<ContractStage> contractStages = contractStageRepo.findAll(specification);

        assertThat(contractStages).usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(contract1.getContractStages());
    }
}
