package com.accountingsystem.repository.custom;

import com.accountingsystem.controller.dtos.ContractDto;
import com.accountingsystem.entitys.Contract;

public interface CustomContractRepo {
    void insertContract(Contract c);
    void updateContract(int contractId, ContractDto c);
}
