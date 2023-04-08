package com.accountingsystem.entitys;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name= "contract_stage")
@Entity
@Data
@EqualsAndHashCode(exclude = {
        "contract"
})
@ToString
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

    @ManyToOne(cascade = {
            CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinColumn(name="contract_id")
    @ToString.Exclude
    private Contract contract;
}
