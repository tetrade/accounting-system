package com.accountingsystem.repository.main;

import com.accountingsystem.entitys.*;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class EntityRelationshipsTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ContractStageRepo contractStageRepo;

    @Autowired
    private ContractRepo contractRepo;

    @Autowired
    private CounterpartyContractRepo counterpartyContractRepo;

    @Autowired
    private CounterpartyOrganizationRepo counterpartyOrganizationRepo;

    @Test
    void shouldCascadeDeleteCounterpartyContractsAndContractStages_whenDeleteContract() {
        Contract shouldBeDeleted = new Contract();
        shouldBeDeleted.setAmount(BigDecimal.ONE);
        shouldBeDeleted.setPlannedEndDate(LocalDate.now());
        shouldBeDeleted.setPlannedStartDate(LocalDate.now());
        shouldBeDeleted.setType(EType.DELIVERY);
        shouldBeDeleted.setName("should_be_deleted");

        Contract shouldNotBeDeleted = new Contract();
        shouldNotBeDeleted.setAmount(BigDecimal.ONE);
        shouldNotBeDeleted.setPlannedEndDate(LocalDate.now());
        shouldNotBeDeleted.setPlannedStartDate(LocalDate.now());
        shouldNotBeDeleted.setType(EType.DELIVERY);
        shouldNotBeDeleted.setName("shouldn't_be_deleted");

        CounterpartyOrganization counterpartyOrganization = new CounterpartyOrganization();
        counterpartyOrganization.setName("co");
        counterpartyOrganization.setAddress("123fss");
        counterpartyOrganization.setInn("123456789012");

        CounterpartyContract deleted = new CounterpartyContract();
        deleted.setAmount(BigDecimal.ZERO);
        deleted.setType(EType.DELIVERY);
        deleted.setPlannedStartDate(LocalDate.now().plusDays(1));
        deleted.setPlannedEndDate(LocalDate.now().minusYears(1));
        deleted.setName("should_be_deleted");
        deleted.setCounterpartyOrganization(counterpartyOrganization);
        shouldBeDeleted.addCounterpartyContract(deleted);

        CounterpartyContract deleted1 = new CounterpartyContract();
        deleted1.setAmount(BigDecimal.TEN);
        deleted1.setType(EType.WORKS);
        deleted1.setPlannedEndDate(LocalDate.now());
        deleted1.setPlannedStartDate(LocalDate.now());
        deleted1.setName("should_be_deleted_1");
        deleted1.setCounterpartyOrganization(counterpartyOrganization);
        shouldBeDeleted.addCounterpartyContract(deleted1);

        CounterpartyContract stay = new CounterpartyContract();
        stay.setAmount(BigDecimal.TEN);
        stay.setType(EType.WORKS);
        stay.setPlannedEndDate(LocalDate.now());
        stay.setPlannedStartDate(LocalDate.now());
        stay.setCounterpartyOrganization(counterpartyOrganization);
        stay.setName("shouldn't_be_deleted");
        shouldNotBeDeleted.addCounterpartyContract(stay);

        ContractStage deleted2 = new ContractStage();
        deleted2.setAmount(BigDecimal.TEN);
        deleted2.setPlannedEndDate(LocalDate.now());
        deleted2.setPlannedStartDate(LocalDate.now());
        deleted2.setPlannedMaterialCosts(BigDecimal.TEN);
        deleted2.setPlannedSalaryExpenses(BigDecimal.ZERO);
        deleted2.setName("should_be_deleted");
        shouldBeDeleted.addContractStage(deleted2);

        ContractStage stay1 = new ContractStage();
        stay1.setAmount(BigDecimal.TEN);
        stay1.setPlannedEndDate(LocalDate.now());
        stay1.setPlannedStartDate(LocalDate.now());
        stay1.setPlannedMaterialCosts(BigDecimal.TEN);
        stay1.setPlannedSalaryExpenses(BigDecimal.ZERO);
        stay1.setName("shouldn't_be_deleted1");
        shouldNotBeDeleted.addContractStage(stay1);

        ContractStage stay2 = new ContractStage();
        stay2.setAmount(BigDecimal.TEN);
        stay2.setPlannedEndDate(LocalDate.now());
        stay2.setPlannedStartDate(LocalDate.now());
        stay2.setPlannedMaterialCosts(BigDecimal.TEN);
        stay2.setPlannedSalaryExpenses(BigDecimal.ZERO);
        stay2.setName("shouldn't_be_deleted");
        shouldNotBeDeleted.addContractStage(stay2);

        contractRepo.save(shouldBeDeleted);
        contractRepo.save(shouldNotBeDeleted);

        // when
        contractRepo.deleteById(shouldBeDeleted.getId());


        assertThat(contractRepo.findAll()).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(shouldNotBeDeleted);
        assertThat(counterpartyContractRepo.findAll()).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(stay);
        assertThat(contractStageRepo.findAll()).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(stay1, stay2);
    }

    @Test
    void shouldNotDeleteOrganization_whenOrganizationWithContract() {
        Contract contractShouldNotBeDeleted = new Contract();
        contractShouldNotBeDeleted.setAmount(BigDecimal.ONE);
        contractShouldNotBeDeleted.setPlannedEndDate(LocalDate.now());
        contractShouldNotBeDeleted.setPlannedStartDate(LocalDate.now());
        contractShouldNotBeDeleted.setType(EType.DELIVERY);
        contractShouldNotBeDeleted.setName("should_be_deleted");

        CounterpartyOrganization organizationShouldNotBeDeleted = new CounterpartyOrganization();
        organizationShouldNotBeDeleted.setName("co");
        organizationShouldNotBeDeleted.setAddress("123fss");
        organizationShouldNotBeDeleted.setInn("123456789012");

        CounterpartyContract counterpartContractShouldNotBeDelete = new CounterpartyContract();
        counterpartContractShouldNotBeDelete.setAmount(BigDecimal.ZERO);
        counterpartContractShouldNotBeDelete.setType(EType.DELIVERY);
        counterpartContractShouldNotBeDelete.setPlannedStartDate(LocalDate.now().plusDays(1));
        counterpartContractShouldNotBeDelete.setPlannedEndDate(LocalDate.now().minusYears(1));
        counterpartContractShouldNotBeDelete.setName("should_be_deleted");
        counterpartContractShouldNotBeDelete.setCounterpartyOrganization(organizationShouldNotBeDeleted);
        contractShouldNotBeDeleted.addCounterpartyContract(counterpartContractShouldNotBeDelete);
        contractRepo.save(contractShouldNotBeDeleted);

        // when
        counterpartyOrganizationRepo.delete(organizationShouldNotBeDeleted);

        assertThat(counterpartyOrganizationRepo.findAll()).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(organizationShouldNotBeDeleted);
        assertThat(contractRepo.findAll()).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(contractShouldNotBeDeleted);
    }

    @Test
    void shouldDeleteOrganization_whenOrganizationDontHaveContracts() {
        CounterpartyOrganization organizationShouldBeDeleted = new CounterpartyOrganization();
        organizationShouldBeDeleted.setName("co");
        organizationShouldBeDeleted.setAddress("123fss");
        organizationShouldBeDeleted.setInn("123456789012");

        CounterpartyOrganization organizationShouldBeNotDeleted = new CounterpartyOrganization();
        organizationShouldBeNotDeleted.setName("co1");
        organizationShouldBeNotDeleted.setAddress("123fs1s");
        organizationShouldBeNotDeleted.setInn("123456789015");

        counterpartyOrganizationRepo.save(organizationShouldBeDeleted);
        counterpartyOrganizationRepo.save(organizationShouldBeNotDeleted);

        // when
        counterpartyOrganizationRepo.delete(organizationShouldBeDeleted);

        assertThat(counterpartyOrganizationRepo.findAll()).usingRecursiveFieldByFieldElementComparator()
                .contains(organizationShouldBeNotDeleted);
    }

    @Test
    void shouldDeleteContract_whenDeleteUser() {
        User userToNotDelete = new User();
        userToNotDelete.setLogin("user_to_not_delete");
        userToNotDelete.setPassword("qwe132");
        userToNotDelete.setFullName("Test");

        Contract contractShouldNotBeDeleted = new Contract();
        contractShouldNotBeDeleted.setAmount(BigDecimal.ONE);
        contractShouldNotBeDeleted.setPlannedEndDate(LocalDate.now());
        contractShouldNotBeDeleted.setPlannedStartDate(LocalDate.now());
        contractShouldNotBeDeleted.setType(EType.DELIVERY);
        contractShouldNotBeDeleted.setName("should_not_be_deleted");
        userToNotDelete.addContract(contractShouldNotBeDeleted);

        User userToDelete = new User();
        userToDelete.setLogin("user_to_delete");
        userToDelete.setPassword("qwe132");
        userToDelete.setFullName("qweqwe");

        Contract contractShouldNotBeDeleted2 = new Contract();
        contractShouldNotBeDeleted2.setAmount(BigDecimal.ONE);
        contractShouldNotBeDeleted2.setPlannedEndDate(LocalDate.now());
        contractShouldNotBeDeleted2.setPlannedStartDate(LocalDate.now());
        contractShouldNotBeDeleted2.setType(EType.DELIVERY);
        contractShouldNotBeDeleted2.setName("should_be_deleted");
        userToDelete.addContract(contractShouldNotBeDeleted2);

        Contract contractShouldNotBeDeleted1 = new Contract();
        contractShouldNotBeDeleted1.setAmount(BigDecimal.ONE);
        contractShouldNotBeDeleted1.setPlannedEndDate(LocalDate.now());
        contractShouldNotBeDeleted1.setPlannedStartDate(LocalDate.now());
        contractShouldNotBeDeleted1.setType(EType.DELIVERY);
        contractShouldNotBeDeleted1.setName("should_be_deleted_1");
        userToDelete.addContract(contractShouldNotBeDeleted1);

        userRepo.save(userToDelete);
        userRepo.save(userToNotDelete);

        // when
        userRepo.delete(userToDelete);

        assertThat(userRepo.findAll()).usingRecursiveFieldByFieldElementComparatorOnFields("login")
                .doesNotContain(userToDelete);

        assertThat(contractRepo.findAll()).usingRecursiveFieldByFieldElementComparator()
                .contains(contractShouldNotBeDeleted, contractShouldNotBeDeleted1, contractShouldNotBeDeleted2);

        assertThat(contractRepo.findAll())
                .filteredOnNull("user")
                .usingRecursiveFieldByFieldElementComparator()
                .containsOnly(contractShouldNotBeDeleted1, contractShouldNotBeDeleted2);
    }
}
