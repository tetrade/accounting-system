package com.accountingsystem.repository.custom;

import com.accountingsystem.controller.dtos.CounterpartyOrganizationDto;

public interface CustomCounterpartyOrganizationRepo {
    void updateCounterpartyOrganization(int counterpartyOrganizationId, CounterpartyOrganizationDto c);
    void insertCounterpartyOrganization(CounterpartyOrganizationDto c);
}
