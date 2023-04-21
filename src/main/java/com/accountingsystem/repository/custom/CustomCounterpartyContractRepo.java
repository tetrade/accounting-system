package com.accountingsystem.repository.custom;

import com.accountingsystem.controller.dtos.CounterpartyContractDto;

public interface CustomCounterpartyContractRepo {
    void insertCounterpartyContract(int counterpartyContractId, CounterpartyContractDto c);
    void updateCounterpartyContract(int counterpartyContractId, CounterpartyContractDto c);
}
