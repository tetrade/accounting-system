package com.accountingsystem.repository.custom.impl;

import com.accountingsystem.controller.dtos.ContractDto;
import com.accountingsystem.entitys.Contract;
import com.accountingsystem.repository.custom.CustomContractRepo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class CustomContractRepoImpl implements CustomContractRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertContract(Contract c) {
        entityManager.persist(c);
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
