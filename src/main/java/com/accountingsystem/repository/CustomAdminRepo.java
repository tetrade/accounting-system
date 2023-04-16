package com.accountingsystem.repository;

import com.accountingsystem.dtos.ContractDto;
import com.accountingsystem.dtos.ContractStageDto;
import com.accountingsystem.dtos.CounterpartyContractDto;
import com.accountingsystem.dtos.CounterpartyOrganizationDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class CustomAdminRepo {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void insertCounterpartyContract(int contractId, CounterpartyContractDto c) {
        entityManager.createNativeQuery("INSERT INTO counterparty_contract (contract_id,counterparty_organization_id,name,amount,actual_start_date," +
            "actual_end_date,planned_start_date,planned_end_date,type) VALUES (?,?,?,?,?,?,?,?,?)")
                .setParameter(1, contractId)
                .setParameter(2, c.getNewCounterpartyOrganizationId())
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
    public void updateCounterpartyContract(int id, CounterpartyContractDto c) {
        entityManager.createNativeQuery("UPDATE counterparty_contract SET counterparty_organization_id = ?, name = ?" +
                        ",amount = ?, actual_start_date = ? , actual_end_date = ?, planned_start_date = ? ," +
                        "planned_end_date = ? ,type = ? WHERE counterparty_contract.id = ?")
                .setParameter(1, c.getNewCounterpartyOrganizationId())
                .setParameter(2, c.getName())
                .setParameter(3, c.getAmount())
                .setParameter(4, c.getActualStartDate())
                .setParameter(5, c.getActualEndDate())
                .setParameter(6, c.getPlannedStartDate())
                .setParameter(7, c.getPlannedEndDate())
                .setParameter(8, c.getType().getType())
                .setParameter(9, id)
                .executeUpdate();
    }

    @Transactional
    public void insertContractStage(int id, ContractStageDto c) {
        entityManager.createNativeQuery("INSERT INTO contract_stage (contract_id, name,amount,actual_start_date, " +
                "actual_end_date,planned_start_date,planned_end_date, actual_salary_expenses, planned_salary_expenses," +
                "actual_material_costs, planned_material_costs) VALUES (?,?,?,?,?,?,?,?,?,?,?)")
                .setParameter(1, id)
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
    public void updateContractStage(int id, ContractStageDto c){
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
                .setParameter(11, id)
                .executeUpdate();
    }

    @Transactional
    public void updateCounterpartyOrganization(int id, CounterpartyOrganizationDto c) {
        entityManager.createNativeQuery("UPDATE counterparty_organization SET name = ?, address = ?, inn = ?" +
                        " WHERE counterparty_organization.id = ?")
                .setParameter(1, c.getName())
                .setParameter(2, c.getAddress())
                .setParameter(3, c.getInn())
                .setParameter(4, id)
                .executeUpdate();
    }

    @Transactional
    public void insertCounterpartyOrganization(CounterpartyOrganizationDto c) {
        entityManager.createNativeQuery("INSERT INTO counterparty_organization (name, address, inn) VALUES (?,?,?)")
                .setParameter(1, c.getName())
                .setParameter(2, c.getAddress())
                .setParameter(3, c.getInn())
                .executeUpdate();
    }

    @Transactional
    public void insertContract(int userId, ContractDto c) {
        entityManager.createNativeQuery("INSERT INTO contract (user_id ,name,amount,actual_start_date," +
                        "actual_end_date,planned_start_date,planned_end_date,type) VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1, userId)
                .setParameter(2, c.getName())
                .setParameter(3, c.getAmount())
                .setParameter(4, c.getActualStartDate())
                .setParameter(5, c.getActualEndDate())
                .setParameter(6, c.getPlannedStartDate())
                .setParameter(7, c.getPlannedEndDate())
                .setParameter(8, c.getType().getType())
                .executeUpdate();
    }

    @Transactional
    public void updateContract(int contractId, ContractDto c) {
        entityManager.createNativeQuery("UPDATE contract SET name = ?" +
                        ",amount = ?, actual_start_date = ? , actual_end_date = ?, planned_start_date = ? ," +
                        "planned_end_date = ? ,type = ? WHERE contract.id = ?")
                .setParameter(1, c.getName())
                .setParameter(2, c.getAmount())
                .setParameter(3, c.getActualStartDate())
                .setParameter(4, c.getActualEndDate())
                .setParameter(5, c.getPlannedStartDate())
                .setParameter(6, c.getPlannedEndDate())
                .setParameter(7, c.getType().getType())
                .setParameter(8, contractId)
                .executeUpdate();
    }
}
