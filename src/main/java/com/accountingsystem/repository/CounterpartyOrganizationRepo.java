package com.accountingsystem.repository;

import com.accountingsystem.entitys.CounterpartyOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterpartyOrganizationRepo extends JpaRepository<CounterpartyOrganization, Integer> {
}
