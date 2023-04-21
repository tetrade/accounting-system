package com.accountingsystem.repository;

import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.repository.custom.CustomContractRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Set;

public interface ContractRepo  extends
        JpaRepository<Contract, Integer>, JpaSpecificationExecutor<Contract> , CustomContractRepo {
    @Query(value=
            " FROM Contract c LEFT JOIN FETCH c.user u LEFT JOIN FETCH u.roles r" +
            " WHERE u.login = :user_login AND c.plannedStartDate >= :start_date AND c.plannedEndDate <= :end_date"
    )
    Set<Contract> getContractsBetweenDatesByLogin(
            @Param("user_login") String login,
            @Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate
    );

    @Modifying
    @Query("delete from Contract c where c.id = :id")
    void deleteById(@Param("id") int id);

    @EntityGraph(value = "Contract.user", type = EntityGraph.EntityGraphType.FETCH)
    Page<Contract> findAll(Specification<Contract> specification, Pageable pageable);
}
