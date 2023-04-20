package com.accountingsystem.repositorie.excel;


import com.accountingsystem.entitys.*;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.repository.ContractStageRepo;
import com.accountingsystem.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@DataJpaTest
class ContractStageRepositoryTest {

    @Autowired
    private ContractStageRepo contractStageRepo;

    @Autowired
    private UserRepo userRepo;


    private User user1;
    private User user2;
    private Contract user1Contract1;
    private Contract user1Contract2;
    private Contract user2Contract3;
    private Contract user2Contract4;
    private ContractStage contract1ContractStage1;
    private ContractStage contract1ContractStage2;
    private ContractStage contract2ContractStage3;
    private ContractStage contract3ContractStage4;

    @BeforeEach
    public void setUp() {
        user1 = new User();
        user1.setLogin("user1");
        user1.setPassword("test");
        user1.setFullName("asd");

        user2 = new User();
        user2.setLogin("user2");
        user2.setPassword("test2");
        user2.setFullName("asd2");

        user1Contract1 = new Contract();
        user1Contract1.setName("test contract");
        user1Contract1.setAmount(BigDecimal.TEN);
        user1Contract1.setType(EType.WORKS);
        user1Contract1.setPlannedStartDate(LocalDate.now());
        user1Contract1.setPlannedEndDate(LocalDate.now());

        user1Contract2 = new Contract();
        user1Contract2.setName("test contract 1");
        user1Contract2.setAmount(BigDecimal.TEN);
        user1Contract2.setType(EType.WORKS);
        user1Contract2.setPlannedStartDate(LocalDate.now());
        user1Contract2.setPlannedEndDate(LocalDate.now());

        user2Contract3 = new Contract();
        user2Contract3.setName("test contract 2");
        user2Contract3.setAmount(BigDecimal.TEN);
        user2Contract3.setType(EType.WORKS);
        user2Contract3.setPlannedStartDate(LocalDate.now());
        user2Contract3.setPlannedEndDate(LocalDate.now());

        user2Contract4 = new Contract();
        user2Contract4.setName("test contract 3");
        user2Contract4.setAmount(BigDecimal.TEN);
        user2Contract4.setType(EType.WORKS);
        user2Contract4.setPlannedStartDate(LocalDate.now());
        user2Contract4.setPlannedEndDate(LocalDate.now());

        contract1ContractStage1 = new ContractStage();
        contract1ContractStage1.setAmount(BigDecimal.TEN);
        contract1ContractStage1.setPlannedEndDate(LocalDate.now());
        contract1ContractStage1.setPlannedStartDate(LocalDate.now());
        contract1ContractStage1.setPlannedMaterialCosts(BigDecimal.TEN);
        contract1ContractStage1.setPlannedSalaryExpenses(BigDecimal.ZERO);
        contract1ContractStage1.setName("contract stage 1");
        user1Contract1.addContractStage(contract1ContractStage1);


        contract1ContractStage2 = new ContractStage();
        contract1ContractStage2.setAmount(BigDecimal.TEN);
        contract1ContractStage2.setPlannedEndDate(LocalDate.now());
        contract1ContractStage2.setPlannedStartDate(LocalDate.now());
        contract1ContractStage2.setPlannedMaterialCosts(BigDecimal.TEN);
        contract1ContractStage2.setPlannedSalaryExpenses(BigDecimal.ZERO);
        contract1ContractStage2.setName("contract stage 2");
        user1Contract1.addContractStage(contract1ContractStage2);

        contract2ContractStage3 = new ContractStage();
        contract2ContractStage3.setAmount(BigDecimal.TEN);
        contract2ContractStage3.setPlannedEndDate(LocalDate.now());
        contract2ContractStage3.setPlannedStartDate(LocalDate.now());
        contract2ContractStage3.setPlannedMaterialCosts(BigDecimal.TEN);
        contract2ContractStage3.setPlannedSalaryExpenses(BigDecimal.ZERO);
        contract2ContractStage3.setName("contract stage 3");
        user1Contract2.addContractStage(contract2ContractStage3);


        contract3ContractStage4 = new ContractStage();
        contract3ContractStage4.setAmount(BigDecimal.TEN);
        contract3ContractStage4.setPlannedEndDate(LocalDate.now());
        contract3ContractStage4.setPlannedStartDate(LocalDate.now());
        contract3ContractStage4.setPlannedMaterialCosts(BigDecimal.TEN);
        contract3ContractStage4.setPlannedSalaryExpenses(BigDecimal.ZERO);
        contract3ContractStage4.setName("contract stage 4");
        user2Contract3.addContractStage(contract3ContractStage4);

        user1.addContract(user1Contract1);
        user1.addContract(user1Contract2);
        user2.addContract(user2Contract3);
        user2.addContract(user2Contract4);

        userRepo.save(user1);
        userRepo.save(user2);
    }

    @Test
    void shouldReturnContractStages_whenExistWithContract() {

        Set<ContractStage> contractStages = contractStageRepo.getContractStagesByContractIdAndUserLogin(
                "user1", user1Contract1.getId()
        );

        Set<ContractStage> contractStages1 = contractStageRepo.getContractStagesByContractIdAndUserLogin(
                "user1", user1Contract2.getId()
        );

        Set<ContractStage> contractStages2 = contractStageRepo.getContractStagesByContractIdAndUserLogin(
                "user2", user2Contract3.getId()
        );

        assertThat(contractStages).usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsExactlyInAnyOrderElementsOf(user1Contract1.getContractStages());


        assertThat(contractStages1).usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsExactlyInAnyOrderElementsOf(user1Contract2.getContractStages());

        assertThat(contractStages2).usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsExactlyInAnyOrderElementsOf(user2Contract3.getContractStages());

    }

    @Test
    void shouldReturnEmptySet_whenContractStagesDontExist() {
        Set<ContractStage> contractStages = contractStageRepo.getContractStagesByContractIdAndUserLogin(
                "user2", user2Contract4.getId()
        );

        assertThat(contractStages).isEmpty();
    }

    @Test
    void shouldReturnEmptySet_whenUserLoginDontOrContractIdDontExist() {
        Set<ContractStage> contractStages = contractStageRepo.getContractStagesByContractIdAndUserLogin(
                "user3", user2Contract4.getId()
        );
        Set<ContractStage> contractStages1 = contractStageRepo.getContractStagesByContractIdAndUserLogin(
                "user2", 100
        );

        assertThat(contractStages).isEmpty();
        assertThat(contractStages1).isEmpty();
    }
}
