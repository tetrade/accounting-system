package com.accountingsystem.repository.custom;

import com.accountingsystem.controller.dtos.ContractStageDto;

public interface CustomContractStageRepo {
    void insertContractStage(int contractId, ContractStageDto c);

    void updateContractStage(int contractStageId, ContractStageDto c);
}
