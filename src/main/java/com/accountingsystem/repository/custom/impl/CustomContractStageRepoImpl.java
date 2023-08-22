package com.accountingsystem.repository.custom.impl;

import com.accountingsystem.controller.dtos.ContractStageDto;
import com.accountingsystem.repository.custom.CustomContractStageRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class CustomContractStageRepoImpl implements CustomContractStageRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertContractStage(int contractId, ContractStageDto c) {
        entityManager.createNativeQuery("INSERT INTO contract_stage (contract_id, name,amount,actual_start_date, " +
                        "actual_end_date,planned_start_date,planned_end_date, actual_salary_expenses, planned_salary_expenses," +
                        "actual_material_costs, planned_material_costs) VALUES (?,?,?,?,?,?,?,?,?,?,?)")
                .setParameter(1, contractId)
                .setParameter(2, c.getName())
                .setParameter(3, c.getAmount())
                .setParameter(4, c.getActualStartDate())
                .setParameter(5, c.getActualEndDate())
                .setParameter(6, c.getPlannedStartDate())
                .setParameter(7, c.getPlannedEndDate())
                .setParameter(8, c.getActualSalaryExpenses())
                .setParameter(9, c.getPlannedSalaryExpenses())
                .setParameter(10, c.getActualMaterialCosts())
                .setParameter(11, c.getPlannedMaterialCosts())
                .executeUpdate();
    }

    @Transactional
    public void updateContractStage(int contractStageId, ContractStageDto c){
        entityManager.createNativeQuery("UPDATE contract_stage SET name = ?, amount = ?, actual_start_date = ?," +
                        "actual_end_date = ?, planned_start_date = ?, planned_end_date = ?, actual_material_costs = ?," +
                        "planned_material_costs = ?, actual_salary_expenses = ?, planned_salary_expenses = ? " +
                        "WHERE contract_stage.id = ?")
                .setParameter(1, c.getName())
                .setParameter(2, c.getAmount())
                .setParameter(3, c.getActualStartDate())
                .setParameter(4, c.getActualEndDate())
                .setParameter(5, c.getPlannedStartDate())
                .setParameter(6, c.getPlannedEndDate())
                .setParameter(7, c.getActualMaterialCosts())
                .setParameter(8, c.getPlannedMaterialCosts())
                .setParameter(9, c.getActualSalaryExpenses())
                .setParameter(10, c.getPlannedSalaryExpenses())
                .setParameter(11, contractStageId)
                .executeUpdate();
    }
}
