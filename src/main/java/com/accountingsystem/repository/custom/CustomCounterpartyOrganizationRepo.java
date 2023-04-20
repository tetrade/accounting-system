package com.accountingsystem.repository.custom;

import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.dtos.CounterpartyOrganizationDto;

public interface CustomCounterpartyOrganizationRepo {
    void updateCounterpartyOrganization(int counterpartyOrganizationId, CounterpartyOrganizationDto c);
    void insertCounterpartyOrganization(CounterpartyOrganizationDto c);
}
