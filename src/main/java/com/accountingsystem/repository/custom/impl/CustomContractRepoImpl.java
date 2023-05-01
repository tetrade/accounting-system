package com.accountingsystem.repository.custom.impl;

import com.accountingsystem.controller.dtos.ContractDto;
import com.accountingsystem.entitys.enums.EType;
import com.accountingsystem.repository.custom.CustomContractRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

public class CustomContractRepoImpl implements CustomContractRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertContract(ContractDto c) {
        entityManager.createNativeQuery("INSERT INTO contract (user_id ,name,amount,actual_start_date," +
                        "actual_end_date,planned_start_date,planned_end_date,type) VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1, c.getUserId())
                .setParameter(2, c.getName())
                .setParameter(3, c.getAmount())
                .setParameter(4, c.getActualStartDate())
                .setParameter(5, c.getActualEndDate())
                .setParameter(6, c.getPlannedStartDate())
                .setParameter(7, c.getPlannedEndDate())
                .setParameter(8, Optional.ofNullable(c.getType()).map(EType::getType).orElse(null))
                .executeUpdate();
    }

    @Transactional
    public void updateContract(int contractId, ContractDto c) {
        entityManager.createNativeQuery("UPDATE contract SET name = ?" +
                        ",amount = ?, actual_start_date = ? , actual_end_date = ?, planned_start_date = ? ," +
                        "planned_end_date = ? , type = ?, user_id = ? WHERE contract.id = ?")
                .setParameter(1, c.getName())
                .setParameter(2, c.getAmount())
                .setParameter(3, c.getActualStartDate())
                .setParameter(4, c.getActualEndDate())
                .setParameter(5, c.getPlannedStartDate())
                .setParameter(6, c.getPlannedEndDate())
                .setParameter(7, c.getType().getType())
                .setParameter(8, c.getUserId())
                .setParameter(9, contractId)
                .executeUpdate();
    }

}
