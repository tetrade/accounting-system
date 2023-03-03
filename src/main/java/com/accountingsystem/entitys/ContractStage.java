package com.accountingsystem.entitys;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name= "contract_stage")
@Entity
@Data
@EqualsAndHashCode(exclude = {
        "contract"
})
public class ContractStage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="planned_start_date")
    private LocalDate plannedStartDate;

    @Column(name="planned_end_date")
    private LocalDate plannedEndDate;

    @Column(name="actual_start_date")
    private LocalDate actualStartDate;

    @Column(name="actual_end_date")
    private LocalDate actualEndDate;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="actual_material_costs")
    private BigDecimal actualMaterialCosts;

    @Column(name="planned_material_costs")
    private BigDecimal plannedMaterialCosts;

    @Column(name="actual_salary_expenses")
    private BigDecimal actualSalaryExpenses;

    @Column(name="planned_salary_expenses")
    private BigDecimal plannedSalaryExpenses;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="contract_id")
    private Contract contract;
}