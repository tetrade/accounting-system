package com.accountingsystem.repository;

import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.repository.custom.CustomCounterpartyContractRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

public interface CounterpartyContractRepo extends
        JpaRepository<CounterpartyContract, Integer>, JpaSpecificationExecutor<CounterpartyContract>,
        CustomCounterpartyContractRepo {

    boolean existsById(int id);

    @Query(value=
            "FROM CounterpartyContract cc LEFT JOIN FETCH cc.contract c LEFT JOIN FETCH c.user u " +
             "WHERE u.login = :user_login AND cc.plannedStartDate >= :start_date AND cc.plannedEndDate <= :end_date"
    )
    LinkedHashSet<CounterpartyContract> getCounterpartyContractsBetweenDatesByLogin(
            @Param("user_login") String login,
            @Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate
    );

    @EntityGraph(value = "CounterpartyContract.counterpartyOrganization", type = EntityGraph.EntityGraphType.FETCH)
    Page<CounterpartyContract> findAll(Specification<CounterpartyContract> specification, Pageable pageable);

    @EntityGraph(value = "CounterpartyContract.counterpartyOrganization", type = EntityGraph.EntityGraphType.FETCH)
    List<CounterpartyContract> findAll(Specification<CounterpartyContract> specification);

    @Modifying
    @Query(value = "delete from counterparty_contract cc where cc.id = :id", nativeQuery = true, countQuery = "select 1")
    void deleteById(@Param("id") Integer id);

}
