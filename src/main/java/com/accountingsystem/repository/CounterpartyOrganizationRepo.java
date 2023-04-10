package com.accountingsystem.repository;

import com.accountingsystem.entitys.CounterpartyOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CounterpartyOrganizationRepo extends
        JpaRepository<CounterpartyOrganization, Integer>, JpaSpecificationExecutor<CounterpartyOrganization> {

}
