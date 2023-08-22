package com.accountingsystem.repository;

import com.accountingsystem.entitys.CounterpartyOrganization;
import com.accountingsystem.repository.custom.CustomCounterpartyOrganizationRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CounterpartyOrganizationRepo extends
        JpaRepository<CounterpartyOrganization, Integer>, JpaSpecificationExecutor<CounterpartyOrganization>,
        CustomCounterpartyOrganizationRepo
{

    @Modifying
    @Query(value = "delete from counterparty_organization co where co.id = :id", nativeQuery = true, countQuery = "select 1")
    void deleteById(@Param("id") Integer id);
}
