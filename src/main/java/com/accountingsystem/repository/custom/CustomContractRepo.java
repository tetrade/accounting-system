package com.accountingsystem.repository.custom;

import com.accountingsystem.dtos.ContractDto;

public interface CustomContractRepo {
    void insertContract(ContractDto c);
    void updateContract(int contractId, ContractDto c);
}
