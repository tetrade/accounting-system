package com.accountingsystem.entitys;

import com.accountingsystem.enums.EType;
import com.accountingsystem.enums.TypeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "counterparty_contract")
@Entity
@Data
@EqualsAndHashCode(exclude = {
        "contract"
})
@ToString
//@DynamicUpdate for big tables
public class CounterpartyContract{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="type")
    @Convert(converter = TypeConverter.class)
    private EType type;

    @ManyToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.PERSIST
    }, fetch = FetchType.LAZY)
    @JoinColumn(name = "counterparty_organization_id")
    private CounterpartyOrganization counterpartyOrganization;

    @ManyToOne(cascade = {
            CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinColumn(name="contract_id")
    @ToString.Exclude
    private Contract contract;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name= "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name= "actual_end_date")
    private LocalDate actualEndDate;

    @Column(name= "planned_start_date")
    private LocalDate plannedStartDate;

    @Column(name= "planned_end_date")
    private LocalDate plannedEndDate;
}
