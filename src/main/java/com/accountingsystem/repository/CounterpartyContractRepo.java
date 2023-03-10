package com.accountingsystem.repository;

import com.accountingsystem.entitys.CounterpartyContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Set;

public interface CounterpartyContractRepo extends JpaRepository<CounterpartyContract, Integer> {

    @Query(value=
            "FROM CounterpartyContract cc LEFT JOIN FETCH cc.contract c LEFT JOIN FETCH c.user u " +
             "WHERE u.login = :user_login AND cc.plannedStartDate >= :start_date AND cc.plannedEndDate <= :end_date"
    )
    Set<CounterpartyContract> getCounterpartyContractsBetweenDatesByLogin(
            @Param("user_login") String login,
            @Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate
    );
}
