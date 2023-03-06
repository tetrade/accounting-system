package com.accountingsystem.entitys;


import com.accountingsystem.enums.EType;
import com.accountingsystem.enums.TypeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Table(name= "contract")
@Entity
@Data
@EqualsAndHashCode(exclude = {
        "user"
})
public class Contract{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="type")
    @Convert(converter = TypeConverter.class)
    private EType type;

    @Column(name="planned_start_date")
    private LocalDate plannedStartDate;

    @Column(name="planned_end_date")
    private LocalDate plannedEndDate;

    @Column(name="actual_start_date")
    private LocalDate actualStartDate;

    @Column(name="actual_end_date")
    private LocalDate actualEndDate;

    @JoinColumn(name="user_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch  = FetchType.LAZY)
    private User user;

    @Column(name="amount")
    private BigDecimal amount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contract", targetEntity = CounterpartyContract.class, fetch = FetchType.LAZY)
    private Set<CounterpartyContract> counterpartyContracts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contract", targetEntity = ContractStage.class, fetch = FetchType.LAZY)
    private Set<ContractStage> contractStages;

    public void addCounterpartyContract(CounterpartyContract counterpartyContract) {
        if (this.counterpartyContracts == null) {
            this.counterpartyContracts = Collections.emptySet();
        }
        this.counterpartyContracts.add(counterpartyContract);
        counterpartyContract.setContract(this);
    }

    public void addContractStage(ContractStage contractStage) {
        if (this.contractStages == null) {
            this.contractStages = Collections.emptySet();
        }
        this.contractStages.add(contractStage);
        contractStage.setContract(this);
    }

    public void removeCounterpartyContract(CounterpartyContract counterpartyContract) {
        this.counterpartyContracts.remove(counterpartyContract);
    }
}
