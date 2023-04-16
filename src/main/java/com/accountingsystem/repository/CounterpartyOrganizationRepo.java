package com.accountingsystem.repository;

import com.accountingsystem.entitys.CounterpartyOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CounterpartyOrganizationRepo extends
        JpaRepository<CounterpartyOrganization, Integer>, JpaSpecificationExecutor<CounterpartyOrganization> {

    @Modifying
    @Query("delete from CounterpartyOrganization  co where co.id = :id")
    void deleteById(@Param("id") int id);
}
