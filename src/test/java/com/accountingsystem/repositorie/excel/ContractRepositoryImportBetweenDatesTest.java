package com.accountingsystem.repositorie.excel;

import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.User;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.AbstractTestContainerStartUp;
import com.accountingsystem.repository.ContractRepo;
import com.accountingsystem.repository.UserLogRepository;
import com.accountingsystem.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class ContractRepositoryImportBetweenDatesTest extends AbstractTestContainerStartUp {

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:8.0");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private UserLogRepository userLogRepository;

    @Autowired
    private ContractRepo contractRepo;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUpUser() {
        User user = new User();
        user.setLogin("User 1");
        user.setPassword("test");
        user.setFullName("asd");

        Contract contract1User = new Contract();
        contract1User.setName("Contract 1 User");
        contract1User.setPlannedStartDate(LocalDate.of(2002, 7, 13));
        contract1User.setPlannedEndDate(LocalDate.of(2022, 7, 13));
        contract1User.setAmount(BigDecimal.TEN);
        contract1User.setType(EType.WORKS);
        user.addContract(contract1User);

        Contract contract2User = new Contract();
        contract2User.setName("Contract 2 User");
        contract2User.setAmount(BigDecimal.TEN);
        contract2User.setType(EType.WORKS);
        contract2User.setPlannedStartDate(LocalDate.of(2005, 1, 1));
        contract2User.setPlannedEndDate(LocalDate.of(2010, 5, 18));
        user.addContract(contract2User);

        userRepo.save(user);
    }

    // getContractsBetweenDatesByLogin() method test

    @Test
    void shouldReturnContracts_whenUserWithSuchContractsExistAndDatesBetween() {
        Contract should1 = new Contract();
        should1.setName("Contract 1 User");

        Contract should2 = new Contract();
        should2.setName("Contract 2 User");

        Set<Contract> contracts = contractRepo.getContractsBetweenDatesByLogin(
                "User 1",
                LocalDate.of(2001, 7, 14),
                LocalDate.of(2022, 8, 14)
        );
        assertThat(contracts)
                .usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsOnly(should1, should2);
    }


    @Test
    void shouldReturnOneContract_whenUserWithSuchContractExistAndDatesBetween() {
        Contract should1 = new Contract();
        should1.setName("Contract 2 User");

        Set<Contract> contracts = contractRepo.getContractsBetweenDatesByLogin(
                "User 1", LocalDate.of(2001, 7, 14),
                LocalDate.of(2021, 8, 14)
        );
        assertThat(contracts)
                .usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsOnly(should1);
    }

    @Test
    void shouldReturnEmptySet_whenUserDontHaveContractBetweenDates() {
        Set<Contract> contracts = contractRepo.getContractsBetweenDatesByLogin(
                "User 1", LocalDate.of(2008, 7, 14),
                LocalDate.of(2022, 8, 14)
        );
        assertThat(contracts).isEmpty();
    }

    @Test
    void shouldReturnEmptySet_whenNoUserWithSuchLogin() {
        Set<Contract> contracts = contractRepo.getContractsBetweenDatesByLogin(
                "User 0", LocalDate.of(1999, 7, 14),
                LocalDate.of(2030, 8, 14)
        );
        assertThat(contracts).isEmpty();
    }
}
