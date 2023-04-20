package com.accountingsystem.repositorie.main;

import com.accountingsystem.dtos.ContractDto;
import com.accountingsystem.dtos.ContractStageDto;
import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.dtos.CounterpartyOrganizationDto;
import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.ContractStage;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.entitys.CounterpartyOrganization;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.repository.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomAdminRepoTest {

    @Autowired
    private CounterpartyOrganizationRepo counterpartyOrganizationRepo;


    @Autowired
    private ContractRepo contractRepo;

    @Autowired
    private ContractStageRepo contractStageRepo;

    @Autowired
    private CounterpartyContractRepo counterpartyContractRepo;

    @Test
    @Order(1)
    void shouldInsertCounterpartyOrganization_whenMethodCalled(){
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
    void shouldUpdateCounterpartyOrganization_whenMethodCalled(){
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

        counterpartyOrganizationRepo.updateCounterpartyOrganization(1, newRow);

        assertThat(counterpartyOrganizationRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(should);
    }

    @Test
    @Order(3)
    void shouldInsertContract_whenMethodCalled() {
        ContractDto c = new ContractDto();
        c.setName("fde1");
        c.setAmount(BigDecimal.valueOf(123.13));
        c.setType(EType.WORKS);
        c.setActualStartDate(LocalDate.of(2002, 2, 10));
        c.setPlannedEndDate(LocalDate.now());
        c.setPlannedStartDate(LocalDate.now().plusDays(2));

        ContractDto c1 = new ContractDto();
        c1.setName("fde9");
        c1.setAmount(BigDecimal.valueOf(123.13));
        c1.setType(EType.WORKS);
        c1.setActualStartDate(LocalDate.of(2002, 2, 10));
        c1.setPlannedEndDate(LocalDate.now());
        c1.setPlannedStartDate(LocalDate.now().plusDays(2));


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

        contractRepo.insertContract(c);
        contractRepo.insertContract(c1);

        assertThat(contractRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "user", "counterpartyContracts", "contractStages")
                .contains(should, should2);
    }

    @Test
    @Order(4)
    void shouldUpdateContract_whenCalled(){
        ContractDto c1 = new ContractDto();
        c1.setName("fde9");
        c1.setAmount(BigDecimal.valueOf(123.13));
        c1.setType(EType.WORKS);
        c1.setActualStartDate(LocalDate.of(2002, 2, 10));
        c1.setPlannedEndDate(LocalDate.now());
        c1.setPlannedStartDate(LocalDate.now().plusDays(2));

        contractRepo.insertContract(c1);

        c1.setType(EType.DELIVERY);
        c1.setActualEndDate(LocalDate.of(2000, 03, 15));

        Contract should = new Contract();
        should.setName(c1.getName());
        should.setAmount(c1.getAmount());
        should.setType(c1.getType());
        should.setActualStartDate(c1.getActualStartDate());
        should.setPlannedEndDate(c1.getPlannedEndDate());
        should.setPlannedStartDate(c1.getPlannedStartDate());
        should.setActualEndDate(c1.getActualEndDate());

        contractRepo.updateContract(1, c1);

        assertThat(contractRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "user", "counterpartyContracts", "contractStages")
                .contains(should);
    }

    @Test
    @Order(5)
    void shouldInsertContractStage_whenMethodCalled(){
        ContractDto c = new ContractDto();
        c.setName("fde1");
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
        cs.setPlannedStartDate(LocalDate.of(2015, 07, 13));
        cs.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        cs.setActualSalaryExpenses(BigDecimal.valueOf(4134.05));
        cs.setPlannedSalaryExpenses(BigDecimal.valueOf(99.22));
        cs.setActualMaterialCosts(BigDecimal.valueOf(10.12));
        cs.setPlannedMaterialCosts(BigDecimal.valueOf(10.12));

        ContractStage should = new ContractStage();
        should.setName("contractS");
        should.setAmount(BigDecimal.valueOf(7652.23));
        should.setActualStartDate(LocalDate.of(2021, 11, 11));
        should.setActualEndDate(LocalDate.of(2020, 10, 10));
        should.setPlannedStartDate(LocalDate.of(2015, 07, 13));
        should.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        should.setActualSalaryExpenses(BigDecimal.valueOf(4134.05));
        should.setPlannedSalaryExpenses(BigDecimal.valueOf(99.22));
        should.setActualMaterialCosts(BigDecimal.valueOf(10.12));
        should.setPlannedMaterialCosts(BigDecimal.valueOf(10.12));

        contractStageRepo.insertContractStage(1, cs);

        assertThat(contractStageRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "contract")
                .contains(should);
    }

    @Test
    @Order(6)
    void shouldUpdateContractStage_whenMethodCalled() {
        ContractDto c = new ContractDto();
        c.setName("fde1");
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
        cs.setPlannedStartDate(LocalDate.of(2015, 07, 13));
        cs.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        cs.setActualSalaryExpenses(BigDecimal.valueOf(4134.05));
        cs.setPlannedSalaryExpenses(BigDecimal.valueOf(99.22));
        cs.setActualMaterialCosts(BigDecimal.valueOf(10.12));
        cs.setPlannedMaterialCosts(BigDecimal.valueOf(10.12));

        contractStageRepo.insertContractStage(1, cs);

        cs.setAmount(BigDecimal.valueOf(9999.98));
        cs.setActualEndDate(LocalDate.of(2002, 02, 02));

        ContractStage should = new ContractStage();
        should.setName("contractS");
        should.setAmount(BigDecimal.valueOf(9999.98));
        should.setActualStartDate(LocalDate.of(2021, 11, 11));
        should.setActualEndDate(LocalDate.of(2020, 10, 10));
        should.setPlannedStartDate(LocalDate.of(2015, 07, 13));
        should.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        should.setActualSalaryExpenses(BigDecimal.valueOf(4134.05));
        should.setPlannedSalaryExpenses(BigDecimal.valueOf(99.22));
        should.setActualMaterialCosts(BigDecimal.valueOf(10.12));
        should.setPlannedMaterialCosts(BigDecimal.valueOf(10.12));
        should.setActualEndDate(LocalDate.of(2002, 02, 02));

        contractStageRepo.updateContractStage(1, cs);

        assertThat(contractStageRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "contract")
                .contains(should);
    }

    @Test
    @Order(7)
    void shouldInsertCounterpartyContract_whenMethodCalled(){
        ContractDto c = new ContractDto();
        c.setName("fde1");
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
        cc.setPlannedStartDate(LocalDate.of(2015, 07, 13));
        cc.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        cc.setCounterpartyOrganizationId(1);
        cc.setType(EType.WORKS);

        CounterpartyContract should = new CounterpartyContract();
        should.setName("contractS");
        should.setAmount(BigDecimal.valueOf(7652.23));
        should.setActualStartDate(LocalDate.of(2021, 11, 11));
        should.setActualEndDate(LocalDate.of(2020, 10, 10));
        should.setPlannedStartDate(LocalDate.of(2015, 07, 13));
        should.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        should.setType(EType.WORKS);

        counterpartyContractRepo.insertCounterpartyContract(1, cc);

        assertThat(counterpartyContractRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "contract", "counterpartyOrganization")
                .contains(should);
    }

    @Test
    @Order(8)
    void shouldUpdateCounterpartyContract_whenMethodCalled() {
        ContractDto c = new ContractDto();
        c.setName("fde1");
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
        cc.setPlannedStartDate(LocalDate.of(2015, 07, 13));
        cc.setPlannedEndDate(LocalDate.of(2019, 3, 15));
        cc.setCounterpartyOrganizationId(1);
        cc.setType(EType.PURCHASE);

        counterpartyContractRepo.insertCounterpartyContract(1, cc);

        cc.setType(EType.DELIVERY);
        cc.setAmount(BigDecimal.valueOf(99.12));

        CounterpartyContract should = new CounterpartyContract();
        should.setName("contractS");
        should.setAmount(BigDecimal.valueOf(7652.23));
        should.setActualStartDate(LocalDate.of(2021, 11, 11));
        should.setActualEndDate(LocalDate.of(2020, 10, 10));
        should.setPlannedStartDate(LocalDate.of(2015, 07, 13));
        should.setPlannedEndDate(LocalDate.of(2019, 3, 15));

        should.setType(EType.DELIVERY);
        should.setAmount(BigDecimal.valueOf(99.12));

        // when
        counterpartyContractRepo.updateCounterpartyContract(1, cc);


        assertThat(counterpartyContractRepo.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "contract", "counterpartyOrganization")
                .contains(should);
    }

}
