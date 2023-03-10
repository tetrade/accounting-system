package com.accountingsystem.repository.excel;

import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.entitys.User;
import com.accountingsystem.repository.CounterpartyContractRepo;
import com.accountingsystem.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class CounterpartyContractRepositoryImportBetweenDatesTest {

    @Autowired
    private CounterpartyContractRepo counterpartyContractRepo;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setLogin("user");

        Contract contract1 = new Contract();
        Contract contract2 = new Contract();

        user.addContract(contract1);
        user.addContract(contract2);

        CounterpartyContract counterpartyContract1 = new CounterpartyContract();
        counterpartyContract1.setName("Counterparty Contract 1");
        counterpartyContract1.setPlannedStartDate(LocalDate.of(2018, 9, 13));
        counterpartyContract1.setPlannedEndDate(LocalDate.of(2020, 8, 9));
        contract1.addCounterpartyContract(counterpartyContract1);

        CounterpartyContract counterpartyContract2 = new CounterpartyContract();
        counterpartyContract2.setName("Counterparty Contract 2");
        counterpartyContract2.setPlannedStartDate(LocalDate.of(2017, 3, 25));
        counterpartyContract2.setPlannedEndDate(LocalDate.of(2022, 8, 8));
        contract1.addCounterpartyContract(counterpartyContract2);

        CounterpartyContract counterpartyContract3 = new CounterpartyContract();
        counterpartyContract3.setName("Counterparty Contract 3");
        counterpartyContract3.setPlannedStartDate(LocalDate.of(2019, 3, 25));
        counterpartyContract3.setPlannedEndDate(LocalDate.of(2021, 8, 8));
        contract1.addCounterpartyContract(counterpartyContract3);

        userRepo.save(user);
    }

    @Test
    public void shouldReturnAllCounterpartyContracts_whenAllCounterpartyContractsOfUserBetweenDates() {
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
    public void shouldReturnOneCounterpartyContracts_whenOneCounterpartyContractsOfUserBetweenDates() {
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
    public void shouldReturnEmptySet_whenNoCounterpartyContractsOfUserBetweenDates() {
        Set<CounterpartyContract> counterpartyContractSet = counterpartyContractRepo.getCounterpartyContractsBetweenDatesByLogin(
                "user", LocalDate.of(2022, 10, 10),
                LocalDate.of(2023, 9, 1)
        );
        assertThat(counterpartyContractSet).isEmpty();
    }

    @Test
    public void shouldReturnEmptySet_whenUserWithLogin() {
        Set<CounterpartyContract> counterpartyContractSet = counterpartyContractRepo.getCounterpartyContractsBetweenDatesByLogin(
                "user none", LocalDate.of(2022, 10, 10),
                LocalDate.of(2023, 9, 1)
        );
        assertThat(counterpartyContractSet).isEmpty();
    }
}





















