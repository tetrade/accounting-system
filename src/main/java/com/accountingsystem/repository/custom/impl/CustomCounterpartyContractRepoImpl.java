package com.accountingsystem.repository.custom.impl;

import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.repository.custom.CustomCounterpartyContractRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class CustomCounterpartyContractRepoImpl implements CustomCounterpartyContractRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertCounterpartyContract(int counterpartyContractId, CounterpartyContractDto c) {
        entityManager.createNativeQuery("INSERT INTO counterparty_contract (contract_id,counterparty_organization_id,name,amount,actual_start_date," +
                        "actual_end_date,planned_start_date,planned_end_date,type) VALUES (?,?,?,?,?,?,?,?,?)")
                .setParameter(1, counterpartyContractId)
                .setParameter(2, c.getCounterpartyOrganizationId())
                .setParameter(3, c.getName())
                .setParameter(4, c.getAmount())
                .setParameter(5, c.getActualStartDate())
                .setParameter(6, c.getActualEndDate())
                .setParameter(7, c.getPlannedStartDate())
                .setParameter(8, c.getPlannedEndDate())
                .setParameter(9, c.getType().getType())
                .executeUpdate();
    }

    @Transactional
    public void updateCounterpartyContract(int counterpartyContractId, CounterpartyContractDto c) {
        entityManager.createNativeQuery("UPDATE counterparty_contract SET counterparty_organization_id = ?, name = ?" +
                        ",amount = ?, actual_start_date = ? , actual_end_date = ?, planned_start_date = ? ," +
                        "planned_end_date = ? ,type = ? WHERE counterparty_contract.id = ?")
                .setParameter(1, c.getCounterpartyOrganizationId())
                .setParameter(2, c.getName())
                .setParameter(3, c.getAmount())
                .setParameter(4, c.getActualStartDate())
                .setParameter(5, c.getActualEndDate())
                .setParameter(6, c.getPlannedStartDate())
                .setParameter(7, c.getPlannedEndDate())
                .setParameter(8, c.getType().getType())
                .setParameter(9, counterpartyContractId)
                .executeUpdate();
    }
}
