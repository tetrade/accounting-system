package com.accountingsystem.repositorie.excel;

import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.entitys.CounterpartyOrganization;
import com.accountingsystem.entitys.User;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.repository.CounterpartyContractRepo;
import com.accountingsystem.repository.UserLogRepository;
import com.accountingsystem.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class CounterpartyContractRepositoryImportBetweenDatesTest {

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private UserLogRepository userLogRepository;

    @Autowired
    private CounterpartyContractRepo counterpartyContractRepo;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUp() {

        CounterpartyOrganization counterpartyOrganization = new CounterpartyOrganization();
        counterpartyOrganization.setInn("012345678912");
        counterpartyOrganization.setAddress("test");
        counterpartyOrganization.setName("org");

        User user = new User();
        user.setLogin("user");
        user.setPassword("test");
        user.setFullName("asd");


        Contract contract1 = new Contract();
        contract1.setName("test contract");
        contract1.setAmount(BigDecimal.TEN);
        contract1.setType(EType.WORKS);
        contract1.setPlannedStartDate(LocalDate.now());
        contract1.setPlannedEndDate(LocalDate.now());

        Contract contract2 = new Contract();
        contract2.setName("test contract 1");
        contract2.setAmount(BigDecimal.TEN);
        contract2.setType(EType.WORKS);
        contract2.setPlannedStartDate(LocalDate.now());
        contract2.setPlannedEndDate(LocalDate.now());

        user.addContract(contract1);
        user.addContract(contract2);

        CounterpartyContract counterpartyContract1 = new CounterpartyContract();
        counterpartyContract1.setName("Counterparty Contract 1");
        counterpartyContract1.setType(EType.DELIVERY);
        counterpartyContract1.setAmount(BigDecimal.ONE);
        counterpartyContract1.setPlannedStartDate(LocalDate.of(2018, 9, 13));
        counterpartyContract1.setPlannedEndDate(LocalDate.of(2020, 8, 9));
        counterpartyContract1.setCounterpartyOrganization(counterpartyOrganization);

        contract1.addCounterpartyContract(counterpartyContract1);


        CounterpartyContract counterpartyContract2 = new CounterpartyContract();
        counterpartyContract2.setName("Counterparty Contract 2");
        counterpartyContract2.setType(EType.DELIVERY);
        counterpartyContract2.setAmount(BigDecimal.ONE);
        counterpartyContract2.setPlannedStartDate(LocalDate.of(2017, 3, 25));
        counterpartyContract2.setPlannedEndDate(LocalDate.of(2022, 8, 8));
        contract1.addCounterpartyContract(counterpartyContract2);
        counterpartyContract2.setCounterpartyOrganization(counterpartyOrganization);

        CounterpartyContract counterpartyContract3 = new CounterpartyContract();
        counterpartyContract3.setName("Counterparty Contract 3");
        counterpartyContract3.setType(EType.DELIVERY);
        counterpartyContract3.setAmount(BigDecimal.ONE);
        counterpartyContract3.setPlannedStartDate(LocalDate.of(2019, 3, 25));
        counterpartyContract3.setPlannedEndDate(LocalDate.of(2021, 8, 8));
        contract1.addCounterpartyContract(counterpartyContract3);
        counterpartyContract3.setCounterpartyOrganization(counterpartyOrganization);

        userRepo.save(user);
    }

    @Test
    void shouldReturnAllCounterpartyContracts_whenAllCounterpartyContractsOfUserBetweenDates() {
        CounterpartyContract should1 = new CounterpartyContract();
        should1.setName("Counterparty Contract 1");
        CounterpartyContract should2 = new CounterpartyContract();
        should2.setName("Counterparty Contract 2");
        CounterpartyContract should3 = new CounterpartyContract();
        should3.setName("Counterparty Contract 3");

        Set<CounterpartyContract> counterpartyContractSet = counterpartyContractRepo.getCounterpartyContractsBetweenDatesByLogin(
                "user", LocalDate.of(2010, 9, 10),
                LocalDate.of(2023, 1, 1)
        );

        assertThat(counterpartyContractSet).usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsOnly(should1, should2, should3);
    }

    @Test
    void shouldReturnOneCounterpartyContracts_whenOneCounterpartyContractsOfUserBetweenDates() {
        CounterpartyContract should1 = new CounterpartyContract();
        should1.setName("Counterparty Contract 1");

        Set<CounterpartyContract> counterpartyContractSet = counterpartyContractRepo.getCounterpartyContractsBetweenDatesByLogin(
                "user", LocalDate.of(2018, 8, 10),
                LocalDate.of(2020, 9, 1)
        );

        assertThat(counterpartyContractSet).usingRecursiveFieldByFieldElementComparatorOnFields("name")
                .containsOnly(should1);
    }

    @Test
    void shouldReturnEmptySet_whenNoCounterpartyContractsOfUserBetweenDates() {
        Set<CounterpartyContract> counterpartyContractSet = counterpartyContractRepo.getCounterpartyContractsBetweenDatesByLogin(
                "user", LocalDate.of(2022, 10, 10),
                LocalDate.of(2023, 9, 1)
        );
        assertThat(counterpartyContractSet).isEmpty();
    }

    @Test
    void shouldReturnEmptySet_whenUserWithLogin() {
        Set<CounterpartyContract> counterpartyContractSet = counterpartyContractRepo.getCounterpartyContractsBetweenDatesByLogin(
                "user none", LocalDate.of(2022, 10, 10),
                LocalDate.of(2023, 9, 1)
        );
        assertThat(counterpartyContractSet).isEmpty();
    }
}





















