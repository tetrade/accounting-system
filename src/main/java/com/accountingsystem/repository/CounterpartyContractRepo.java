package com.accountingsystem.repository;

import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.repository.custom.CustomCounterpartyContractRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface CounterpartyContractRepo extends
        JpaRepository<CounterpartyContract, Integer>, JpaSpecificationExecutor<CounterpartyContract>,
        CustomCounterpartyContractRepo {

    boolean existsById(int id);

    @Query(value=
            "FROM CounterpartyContract cc LEFT JOIN FETCH cc.contract c LEFT JOIN FETCH c.user u " +
             "WHERE u.login = :user_login AND cc.plannedStartDate >= :start_date AND cc.plannedEndDate <= :end_date"
    )
    Set<CounterpartyContract> getCounterpartyContractsBetweenDatesByLogin(
            @Param("user_login") String login,
            @Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate
    );

    @EntityGraph(value = "CounterpartyContract.counterpartyOrganization", type = EntityGraph.EntityGraphType.FETCH)
    Page<CounterpartyContract> findAll(Specification<CounterpartyContract> specification, Pageable pageable);

    @EntityGraph(value = "CounterpartyContract.counterpartyOrganization", type = EntityGraph.EntityGraphType.FETCH)
    List<CounterpartyContract> findAll(Specification<CounterpartyContract> specification);

    @Modifying
    @Query(value="delete from CounterpartyContract cc where cc.id = :id")
    void deleteById(@Param("id") int id);

}
