package com.accountingsystem.repositorie.main;

import com.accountingsystem.AbstractTestContainerStartUp;
import com.accountingsystem.controller.dtos.ContractDto;
import com.accountingsystem.controller.dtos.ContractStageDto;
import com.accountingsystem.controller.dtos.CounterpartyContractDto;
import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;


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

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class CustomAdminRepoTest extends AbstractTestContainerStartUp {

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private UserLogRepository userLogRepository;

    @Autowired
    private CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    @Autowired
    private ContractRepo contractRepo;

    @Autowired
    private ContractStageRepo contractStageRepo;

    @Autowired
    private CounterpartyContractRepo counterpartyContractRepo;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepo userRepo;

    @Test
    @Order(1)
    void shouldInsertCounterpartyOrganization_whenMethodCalled() {
        CounterpartyOrganizationDto newRow = new CounterpartyOrganizationDto();
        newRow.setName("name");
        newRow.setAddress("address");
        newRow.setInn("012345678912");

        CounterpartyOrganization should = new CounterpartyOrganization();
        should.setInn(newRow.getInn());
        should.setName(newRow.getName());
        should.setAddress(newRow.getAddress());
        should.setCounterpartyContracts(new HashSet<>());

        counterpartyOrganizationRepo.insertCounterpartyOrganization(newRow);

        assertThat(counterpartyOrganizationRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(should);
    }

    @Test
    @Order(2)
    void shouldUpdateCounterpartyOrganization_whenMethodCalled() {
        CounterpartyOrganizationDto newRow = new CounterpartyOrganizationDto();
        newRow.setName("name123");
        newRow.setAddress("addres");
        newRow.setInn("012345678912");

        counterpartyOrganizationRepo.insertCounterpartyOrganization(newRow);

        newRow.setName("new-name");
        newRow.setAddress("new-addres");

        CounterpartyOrganization should = new CounterpartyOrganization();
        should.setInn(newRow.getInn());
        should.setName(newRow.getName());
        should.setAddress(newRow.getAddress());
        should.setCounterpartyContracts(new HashSet<>());

        CounterpartyOrganization c = counterpartyOrganizationRepo.findAll().get(0);
        counterpartyOrganizationRepo.updateCounterpartyOrganization(c.getId(), newRow);
        entityManager.refresh(c);

        assertThat(Stream.of(c))
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(should);
    }

    @Test
    @Order(3)
    void shouldInsertContract_whenMethodCalled() {
        Contract should = new Contract();
        should.setName("fde1");
        should.setAmount(BigDecimal.valueOf(123.13));
        should.setType(EType.WORKS);
        should.setActualStartDate(LocalDate.of(2002, 2, 10));
        should.setPlannedEndDate(LocalDate.now());
        should.setPlannedStartDate(LocalDate.now().plusDays(2));

        Contract should2 = new Contract();
        should2.setName("fde9");
        should2.setAmount(BigDecimal.valueOf(123.13));
        should2.setType(EType.WORKS);
        should2.setActualStartDate(LocalDate.of(2002, 2, 10));
        should2.setPlannedEndDate(LocalDate.now());
        should2.setPlannedStartDate(LocalDate.now().plusDays(2));

        contractRepo.insertContract(should);
        contractRepo.insertContract(should2);

        assertThat(contractRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "user", "counterpartyContracts", "contractStages")
                .contains(should, should2);
    }

    @Test
    @Order(4)
    void shouldUpdateContract_whenCalled() {
        Contract shouldBeUpdated = new Contract();
        shouldBeUpdated.setName("fde9");
        shouldBeUpdated.setAmount(BigDecimal.valueOf(123.13));
        shouldBeUpdated.setType(EType.WORKS);
        shouldBeUpdated.setActualStartDate(LocalDate.of(2002, 2, 10));
        shouldBeUpdated.setPlannedEndDate(LocalDate.now());
        shouldBeUpdated.setPlannedStartDate(LocalDate.now().plusDays(2));

        contractRepo.insertContract(shouldBeUpdated);
        ContractDto c = new ContractDto();
        c.setName(shouldBeUpdated.getName());
        c.setAmount(shouldBeUpdated.getAmount());
        c.setType(EType.DELIVERY);
        c.setActualStartDate(LocalDate.of(2002, 2, 11));
        c.setPlannedEndDate(shouldBeUpdated.getPlannedEndDate());
        c.setPlannedStartDate(shouldBeUpdated.getPlannedStartDate());

        Contract should = new Contract();
        should.setName(shouldBeUpdated.getName());
        should.setAmount(shouldBeUpdated.getAmount());
        should.setType(EType.DELIVERY);
        should.setActualStartDate(LocalDate.of(2002, 2, 11));
        should.setPlannedEndDate(shouldBeUpdated.getPlannedEndDate());
        should.setPlannedStartDate(shouldBeUpdated.getPlannedStartDate());

        Contract contract = contractRepo.findAll().get(0);
        contractRepo.updateContract(contract.getId(), c);
        entityManager.refresh(contract);

        assertThat(Stream.of(contract))
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "user", "counterpartyContracts", "contractStages")
                .contains(should);
    }

    @Test
    @Order(5)
    void shouldInsertContractStage_whenMethodCalled() {
        Contract c = new Contract();
        c.setName("fde9");
        c.setAmount(BigDecimal.valueOf(123.13));
        c.setType(EType.WORKS);
        c.setActualStartDate(LocalDate.of(2002, 2, 10));
        c.setPlannedEndDate(LocalDate.now());
        c.setPlannedStartDate(LocalDate.now().plusDays(2));

        contractRepo.insertContract(c);

        ContractStageDto cs = new ContractStageDto();
        cs.setName("contractS");
        cs.setAmount(BigDecimal.valueOf(7652.23));
        cs.setActualStartDate(LocalDate.of(2021, 11, 11));
        cs.setActualEndDate(LocalDate.of(2020, 10, 10));
        cs.setPlannedStartDate(LocalDate.of(2015, 7, 13));
        cs.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        cs.setActualSalaryExpenses(BigDecimal.valueOf(4134.05));
        cs.setPlannedSalaryExpenses(BigDecimal.valueOf(99.22));
        cs.setActualMaterialCosts(BigDecimal.valueOf(10.12));
        cs.setPlannedMaterialCosts(BigDecimal.valueOf(10.12));
        cs.setId(4);

        ContractStage should = new ContractStage();
        should.setName("contractS");
        should.setAmount(BigDecimal.valueOf(7652.23));
        should.setActualStartDate(LocalDate.of(2021, 11, 11));
        should.setActualEndDate(LocalDate.of(2020, 10, 10));
        should.setPlannedStartDate(LocalDate.of(2015, 7, 13));
        should.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        should.setActualSalaryExpenses(BigDecimal.valueOf(4134.05));
        should.setPlannedSalaryExpenses(BigDecimal.valueOf(99.22));
        should.setActualMaterialCosts(BigDecimal.valueOf(10.12));
        should.setPlannedMaterialCosts(BigDecimal.valueOf(10.12));

        Contract contract = contractRepo.findAll().get(0);
        contractStageRepo.insertContractStage(contract.getId(), cs);

        assertThat(contractStageRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "contract")
                .contains(should);
    }

    @Test
    @Order(6)
    void shouldUpdateContractStage_whenMethodCalled() {
        Contract c = new Contract();
        c.setName("fde9");
        c.setAmount(BigDecimal.valueOf(123.13));
        c.setType(EType.WORKS);
        c.setActualStartDate(LocalDate.of(2002, 2, 10));
        c.setPlannedEndDate(LocalDate.now());
        c.setPlannedStartDate(LocalDate.now().plusDays(2));

        contractRepo.insertContract(c);

        ContractStageDto cs = new ContractStageDto();
        cs.setName("contractS");
        cs.setAmount(BigDecimal.valueOf(7652.23));
        cs.setActualStartDate(LocalDate.of(2021, 11, 11));
        cs.setActualEndDate(LocalDate.of(2020, 10, 10));
        cs.setPlannedStartDate(LocalDate.of(2015, 7, 13));
        cs.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        cs.setActualSalaryExpenses(BigDecimal.valueOf(4134.05));
        cs.setPlannedSalaryExpenses(BigDecimal.valueOf(99.22));
        cs.setActualMaterialCosts(BigDecimal.valueOf(10.12));
        cs.setPlannedMaterialCosts(BigDecimal.valueOf(10.12));

        Contract contract = contractRepo.findAll().get(0);
        contractStageRepo.insertContractStage(contract.getId(), cs);

        cs.setAmount(BigDecimal.valueOf(9999.98));
        cs.setActualEndDate(LocalDate.of(2002, 2, 2));

        ContractStage should = new ContractStage();
        should.setName("contractS");
        should.setAmount(BigDecimal.valueOf(9999.98));
        should.setActualStartDate(LocalDate.of(2021, 11, 11));
        should.setActualEndDate(LocalDate.of(2020, 10, 10));
        should.setPlannedStartDate(LocalDate.of(2015, 7, 13));
        should.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        should.setActualSalaryExpenses(BigDecimal.valueOf(4134.05));
        should.setPlannedSalaryExpenses(BigDecimal.valueOf(99.22));
        should.setActualMaterialCosts(BigDecimal.valueOf(10.12));
        should.setPlannedMaterialCosts(BigDecimal.valueOf(10.12));
        should.setActualEndDate(LocalDate.of(2002, 2, 2));

        ContractStage contractStage = contractStageRepo.findAll().get(0);
        contractStageRepo.updateContractStage(contractStage.getId(), cs);
        entityManager.refresh(contractStage);

        assertThat(contractStageRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "contract")
                .contains(should);
    }

    @Test
    @Order(7)
    void shouldInsertCounterpartyContract_whenMethodCalled() {
        Contract c = new Contract();
        c.setName("fde9");
        c.setAmount(BigDecimal.valueOf(123.13));
        c.setType(EType.WORKS);
        c.setActualStartDate(LocalDate.of(2002, 2, 10));
        c.setPlannedEndDate(LocalDate.now());
        c.setPlannedStartDate(LocalDate.now().plusDays(2));

        contractRepo.insertContract(c);

        CounterpartyOrganizationDto newRow = new CounterpartyOrganizationDto();
        newRow.setName("name");
        newRow.setAddress("address");
        newRow.setInn("012345678912");

        counterpartyOrganizationRepo.insertCounterpartyOrganization(newRow);

        CounterpartyContractDto cc = new CounterpartyContractDto();
        cc.setName("contractS");
        cc.setAmount(BigDecimal.valueOf(7652.23));
        cc.setActualStartDate(LocalDate.of(2021, 11, 11));
        cc.setActualEndDate(LocalDate.of(2020, 10, 10));
        cc.setPlannedStartDate(LocalDate.of(2015, 7, 13));
        cc.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        cc.setCounterpartyOrganizationId(counterpartyOrganizationRepo.findAll().get(0).getId());
        cc.setType(EType.WORKS);

        CounterpartyContract should = new CounterpartyContract();
        should.setName("contractS");
        should.setAmount(BigDecimal.valueOf(7652.23));
        should.setActualStartDate(LocalDate.of(2021, 11, 11));
        should.setActualEndDate(LocalDate.of(2020, 10, 10));
        should.setPlannedStartDate(LocalDate.of(2015, 7, 13));
        should.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        should.setType(EType.WORKS);

        Contract contract = contractRepo.findAll().get(0);
        counterpartyContractRepo.insertCounterpartyContract(contract.getId(), cc);

        assertThat(counterpartyContractRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "contract", "counterpartyOrganization")
                .contains(should);
    }

    @Test
    @Order(8)
    void shouldUpdateCounterpartyContract_whenMethodCalled() {
        Contract c = new Contract();
        c.setName("fde9");
        c.setAmount(BigDecimal.valueOf(123.13));
        c.setType(EType.WORKS);
        c.setActualStartDate(LocalDate.of(2002, 2, 10));
        c.setPlannedEndDate(LocalDate.now());
        c.setPlannedStartDate(LocalDate.now().plusDays(2));

        contractRepo.insertContract(c);

        CounterpartyOrganizationDto newRow = new CounterpartyOrganizationDto();
        newRow.setName("name");
        newRow.setAddress("address");
        newRow.setInn("012345678912");
        counterpartyOrganizationRepo.insertCounterpartyOrganization(newRow);

        CounterpartyContractDto cc = new CounterpartyContractDto();
        cc.setName("contractS");
        cc.setAmount(BigDecimal.valueOf(7652.23));
        cc.setActualStartDate(LocalDate.of(2021, 11, 11));
        cc.setActualEndDate(LocalDate.of(2020, 10, 10));
        cc.setPlannedStartDate(LocalDate.of(2015, 7, 13));
        cc.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        cc.setCounterpartyOrganizationId(counterpartyOrganizationRepo.findAll().get(0).getId());
        cc.setType(EType.PURCHASE);

        Contract contract = contractRepo.findAll().get(0);
        counterpartyContractRepo.insertCounterpartyContract(contract.getId(), cc);

        cc.setType(EType.DELIVERY);
        cc.setAmount(BigDecimal.valueOf(99.12));

        CounterpartyContract should = new CounterpartyContract();
        should.setName("contractS");
        should.setAmount(BigDecimal.valueOf(7652.23));
        should.setActualStartDate(LocalDate.of(2021, 11, 11));
        should.setActualEndDate(LocalDate.of(2020, 10, 10));
        should.setPlannedStartDate(LocalDate.of(2015, 7, 13));
        should.setPlannedEndDate(LocalDate.of(2019, 3, 15));

        should.setType(EType.DELIVERY);
        should.setAmount(BigDecimal.valueOf(99.12));

        // when
        CounterpartyContract counterpartyContract = counterpartyContractRepo.findAll().get(0);
        counterpartyContractRepo.updateCounterpartyContract(counterpartyContract.getId(), cc);
        entityManager.refresh(counterpartyContract);

        assertThat(Stream.of(counterpartyContract))
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "contract", "counterpartyOrganization")
                .contains(should);
    }


    @Test
    @Order(9)
    void shouldCreateContractWithCounterpartyContractsAndContractsStage_whenMethodCalled() {
        Contract c = new Contract();
        c.setName("fde9");
        c.setAmount(BigDecimal.valueOf(123.13));
        c.setType(EType.WORKS);
        c.setActualStartDate(LocalDate.of(2002, 2, 10));
        c.setPlannedEndDate(LocalDate.now());
        c.setPlannedStartDate(LocalDate.now().plusDays(2));

        ContractStage cs = new ContractStage();
        cs.setContract(c);
        cs.setName("test1");
        cs.setAmount(BigDecimal.valueOf(123));
        cs.setPlannedMaterialCosts(BigDecimal.valueOf(123.9));
        cs.setPlannedEndDate(LocalDate.now());
        cs.setPlannedStartDate(LocalDate.now().minusWeeks(12));
        cs.setPlannedSalaryExpenses(BigDecimal.valueOf(9));

        ContractStage cs1 = new ContractStage();
        cs1.setContract(c);
        cs1.setName("test2");
        cs1.setAmount(BigDecimal.valueOf(89));
        cs1.setPlannedMaterialCosts(BigDecimal.valueOf(100000.23));
        cs1.setPlannedEndDate(LocalDate.now());
        cs1.setPlannedStartDate(LocalDate.now().plusDays(12));
        cs1.setPlannedSalaryExpenses(BigDecimal.valueOf(9));
        cs1.setActualEndDate(LocalDate.of(2002, 9, 9));

        CounterpartyOrganization counterpartyOrganization = new CounterpartyOrganization();
        counterpartyOrganization.setName("test4");
        counterpartyOrganization.setInn("012345678901");
        counterpartyOrganization.setAddress("test5");

        CounterpartyContract cc = new CounterpartyContract();
        cc.setContract(c);
        cc.setType(EType.DELIVERY);
        cc.setName("test3");
        cc.setAmount(BigDecimal.valueOf(89));
        cc.setPlannedEndDate(LocalDate.now());
        cc.setPlannedStartDate(LocalDate.now().plusDays(12));
        cc.setActualEndDate(LocalDate.of(2002, 9, 9));
        cc.setCounterpartyOrganization(counterpartyOrganization);

        c.setCounterpartyContracts(Stream.of(cc).collect(Collectors.toSet()));
        c.setContractStages(Stream.of(cs, cs1).collect(Collectors.toSet()));

        contractRepo.insertContract(c);

        assertThat(contractRepo.findAll()).
                usingRecursiveFieldByFieldElementComparator().contains(c);
    }

    @Test
    @Order(10)
    void shouldDeleteCounterpartyOrganization_whenMethodCalled() {
        CounterpartyOrganizationDto counterpartyOrganization = new CounterpartyOrganizationDto();
        counterpartyOrganization.setName("test4");
        counterpartyOrganization.setInn("012345678901");
        counterpartyOrganization.setAddress("test5");

        counterpartyOrganizationRepo.insertCounterpartyOrganization(counterpartyOrganization);

        List<CounterpartyOrganization> counterpartyOrganizationList = counterpartyOrganizationRepo.findAll();
        assertThat(counterpartyOrganizationList).hasSize(1);


        counterpartyOrganizationRepo.deleteById(counterpartyOrganizationList.get(0).getId());

        assertThat(counterpartyOrganizationRepo.findAll()).isEmpty();
    }

    @Test
    @Order(11)
    void shouldDeleteCounterpartyContract_whenMethodCalled() {
        Contract c = new Contract();
        c.setName("fde9");
        c.setAmount(BigDecimal.valueOf(123.13));
        c.setType(EType.WORKS);
        c.setActualStartDate(LocalDate.of(2002, 2, 10));
        c.setPlannedEndDate(LocalDate.now());
        c.setPlannedStartDate(LocalDate.now().plusDays(2));

        ContractStage cs = new ContractStage();
        cs.setContract(c);
        cs.setName("test1");
        cs.setAmount(BigDecimal.valueOf(123));
        cs.setPlannedMaterialCosts(BigDecimal.valueOf(123.9));
        cs.setPlannedEndDate(LocalDate.now());
        cs.setPlannedStartDate(LocalDate.now().minusWeeks(12));
        cs.setPlannedSalaryExpenses(BigDecimal.valueOf(9));

        ContractStage cs1 = new ContractStage();
        cs1.setContract(c);
        cs1.setName("test2");
        cs1.setAmount(BigDecimal.valueOf(89));
        cs1.setPlannedMaterialCosts(BigDecimal.valueOf(100000.23));
        cs1.setPlannedEndDate(LocalDate.now());
        cs1.setPlannedStartDate(LocalDate.now().plusDays(12));
        cs1.setPlannedSalaryExpenses(BigDecimal.valueOf(9));
        cs1.setActualEndDate(LocalDate.of(2002, 9, 9));

        CounterpartyOrganization counterpartyOrganization = new CounterpartyOrganization();
        counterpartyOrganization.setName("test4");
        counterpartyOrganization.setInn("012345678901");
        counterpartyOrganization.setAddress("test5");

        CounterpartyContract cc = new CounterpartyContract();
        cc.setContract(c);
        cc.setType(EType.DELIVERY);
        cc.setName("test3");
        cc.setAmount(BigDecimal.valueOf(89));
        cc.setPlannedEndDate(LocalDate.now());
        cc.setPlannedStartDate(LocalDate.now().plusDays(12));
        cc.setActualEndDate(LocalDate.of(2002, 9, 9));
        cc.setCounterpartyOrganization(counterpartyOrganization);

        c.setCounterpartyContracts(Stream.of(cc).collect(Collectors.toSet()));
        c.setContractStages(Stream.of(cs, cs1).collect(Collectors.toSet()));

        contractRepo.insertContract(c);


        List<CounterpartyContract> counterpartyContractList = counterpartyContractRepo.findAll();
        assertThat(counterpartyContractList).hasSize(1)
                .usingRecursiveFieldByFieldElementComparator().contains(cc);

        counterpartyContractRepo.deleteById(counterpartyContractList.get(0).getId());

        assertThat(counterpartyContractRepo.findAll()).isEmpty();

        assertThat(contractRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("counterpartyContracts")
                .contains(c);
    }


    @Test
    @Order(12)
    void shouldDeleteContractStage_whenCalled() {
        Contract c = new Contract();
        c.setName("fde9");
        c.setAmount(BigDecimal.valueOf(123.13));
        c.setType(EType.WORKS);
        c.setActualStartDate(LocalDate.of(2002, 2, 10));
        c.setPlannedEndDate(LocalDate.now());
        c.setPlannedStartDate(LocalDate.now().plusDays(2));

        ContractStage cs = new ContractStage();
        cs.setContract(c);
        cs.setName("test1");
        cs.setAmount(BigDecimal.valueOf(123));
        cs.setPlannedMaterialCosts(BigDecimal.valueOf(123.9));
        cs.setPlannedEndDate(LocalDate.now());
        cs.setPlannedStartDate(LocalDate.now().minusWeeks(12));
        cs.setPlannedSalaryExpenses(BigDecimal.valueOf(9));

        ContractStage cs1 = new ContractStage();
        cs1.setContract(c);
        cs1.setName("test2");
        cs1.setAmount(BigDecimal.valueOf(89));
        cs1.setPlannedMaterialCosts(BigDecimal.valueOf(100000.23));
        cs1.setPlannedEndDate(LocalDate.now());
        cs1.setPlannedStartDate(LocalDate.now().plusDays(12));
        cs1.setPlannedSalaryExpenses(BigDecimal.valueOf(9));
        cs1.setActualEndDate(LocalDate.of(2002, 9, 9));

        CounterpartyOrganization counterpartyOrganization = new CounterpartyOrganization();
        counterpartyOrganization.setName("test4");
        counterpartyOrganization.setInn("012345678901");
        counterpartyOrganization.setAddress("test5");

        CounterpartyContract cc = new CounterpartyContract();
        cc.setContract(c);
        cc.setType(EType.DELIVERY);
        cc.setName("test3");
        cc.setAmount(BigDecimal.valueOf(89));
        cc.setPlannedEndDate(LocalDate.now());
        cc.setPlannedStartDate(LocalDate.now().plusDays(12));
        cc.setActualEndDate(LocalDate.of(2002, 9, 9));
        cc.setCounterpartyOrganization(counterpartyOrganization);

        c.setCounterpartyContracts(Stream.of(cc).collect(Collectors.toSet()));
        c.setContractStages(Stream.of(cs, cs1).collect(Collectors.toSet()));

        contractRepo.insertContract(c);

        List<ContractStage> contractStageList = contractStageRepo.findAll();
        assertThat(contractStageList).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator().contains(cs, cs1);

        contractStageRepo.deleteById(contractStageList.get(0).getId());

        assertThat(contractStageRepo.findAll()).hasSize(1)
                .usingRecursiveFieldByFieldElementComparator().containsAnyOf(cs, cs1);

        assertThat(contractRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("contractStages")
                .contains(c);
    }

    @Test
    @Order(13)
    void shouldDeleteContractAndCascadeDeleteCounterpartyContractAndContractStage_whenMethodCalled() {
        User user = new User();
        user.setPassword("qqwe1223");
        user.setLogin("qwwer123");
        user.setFullName("tEST user user");

        userRepo.save(user);

        Contract c = new Contract();
        c.setName("fde9");
        c.setAmount(BigDecimal.valueOf(123.13));
        c.setType(EType.WORKS);
        c.setActualStartDate(LocalDate.of(2002, 2, 10));
        c.setPlannedEndDate(LocalDate.now());
        c.setPlannedStartDate(LocalDate.now().plusDays(2));
        c.setUser(user);

        Contract c1 = new Contract();
        c1.setName("qewww");
        c1.setAmount(BigDecimal.valueOf(999.89));
        c1.setType(EType.WORKS);
        c1.setActualStartDate(LocalDate.of(2002, 2, 10));
        c1.setPlannedEndDate(LocalDate.now());
        c1.setPlannedStartDate(LocalDate.now().plusDays(2));
        c1.setUser(user);

        Contract c2 = new Contract();
        c2.setName("eeeeeqwe123");
        c2.setAmount(BigDecimal.valueOf(99012.12));
        c2.setType(EType.WORKS);
        c2.setActualStartDate(LocalDate.of(2002, 5, 19));
        c2.setPlannedEndDate(LocalDate.now());
        c2.setPlannedStartDate(LocalDate.now().plusDays(290));
        c2.setActualEndDate(LocalDate.of(2005, 10, 10));
        c2.setUser(user);

        ContractStage cs = new ContractStage();
        cs.setContract(c);
        cs.setName("test1");
        cs.setAmount(BigDecimal.valueOf(123));
        cs.setPlannedMaterialCosts(BigDecimal.valueOf(123.9));
        cs.setPlannedEndDate(LocalDate.now());
        cs.setPlannedStartDate(LocalDate.now().minusWeeks(12));
        cs.setPlannedSalaryExpenses(BigDecimal.valueOf(9));

        ContractStage cs1 = new ContractStage();
        cs1.setContract(c);
        cs1.setName("test2");
        cs1.setAmount(BigDecimal.valueOf(89));
        cs1.setPlannedMaterialCosts(BigDecimal.valueOf(100000.23));
        cs1.setPlannedEndDate(LocalDate.now());
        cs1.setPlannedStartDate(LocalDate.now().plusDays(12));
        cs1.setPlannedSalaryExpenses(BigDecimal.valueOf(9));
        cs1.setActualEndDate(LocalDate.of(2002, 9, 9));

        CounterpartyOrganization counterpartyOrganization = new CounterpartyOrganization();
        counterpartyOrganization.setName("test4");
        counterpartyOrganization.setInn("012345678901");
        counterpartyOrganization.setAddress("test5");

        CounterpartyContract cc = new CounterpartyContract();
        cc.setContract(c);
        cc.setType(EType.DELIVERY);
        cc.setName("test3");
        cc.setAmount(BigDecimal.valueOf(89));
        cc.setPlannedEndDate(LocalDate.now());
        cc.setPlannedStartDate(LocalDate.now().plusDays(12));
        cc.setActualEndDate(LocalDate.of(2002, 9, 9));
        cc.setCounterpartyOrganization(counterpartyOrganization);

        c.setCounterpartyContracts(Stream.of(cc).collect(Collectors.toSet()));
        c.setContractStages(Stream.of(cs, cs1).collect(Collectors.toSet()));

        contractRepo.insertContract(c);
        contractRepo.insertContract(c1);
        contractRepo.insertContract(c2);

        List<Contract> contractList = contractRepo.findAll();

        assertThat(contractList).usingRecursiveFieldByFieldElementComparator().contains(c, c1, c2);
        assertThat(counterpartyContractRepo.findAll()).usingRecursiveFieldByFieldElementComparator().contains(cc);
        assertThat(contractStageRepo.findAll()).usingRecursiveFieldByFieldElementComparator().contains(cs, cs1);
        assertThat(counterpartyOrganizationRepo.findAll()).usingRecursiveFieldByFieldElementComparator().contains(counterpartyOrganization);

        contractRepo.deleteById(contractList.get(0).getId());

        assertThat(contractRepo.findAll()).usingRecursiveFieldByFieldElementComparator().contains(c1, c2);
        assertThat(counterpartyContractRepo.findAll()).isEmpty();
        assertThat(contractStageRepo.findAll()).isEmpty();
        assertThat(counterpartyOrganizationRepo.findAll()).usingRecursiveFieldByFieldElementComparator().contains(counterpartyOrganization);
        assertThat(userRepo.findAll()).usingRecursiveFieldByFieldElementComparator().contains(user);
    }


    @Test
    @Order(14)
    void shouldDeleteUser_whenMethodCalled() {
        User user = new User();
        user.setPassword("qqwe1223");
        user.setLogin("qwwer123");
        user.setFullName("tEST user user");

        User u = userRepo.save(user);

        assertThat(userRepo.findAll()).usingRecursiveFieldByFieldElementComparator().contains(user);

        userRepo.deleteById(u.getId());

        assertThat(userRepo.findAll()).hasSize(1).doesNotContain(user);
    }
}
