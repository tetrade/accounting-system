package com.accountingsystem.entitys;


import com.accountingsystem.entitys.enums.EType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Table(name = "contract")
@Entity
@Data
@ToString
@EqualsAndHashCode(exclude = {
        "user"
})
@NamedEntityGraph(
        name = "Contract.user",
        attributeNodes = @NamedAttributeNode("user")
)
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Convert(converter = TypeConverter.class)
    private EType type;

    @Column(name = "planned_start_date")
    private LocalDate plannedStartDate;

    @Column(name = "planned_end_date")
    private LocalDate plannedEndDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    @ToString.Exclude
    private User user;

    @Column(name = "amount")
    private BigDecimal amount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contract", targetEntity = CounterpartyContract.class)
    @ToString.Exclude
    private Set<CounterpartyContract> counterpartyContracts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contract", targetEntity = ContractStage.class)
    @ToString.Exclude
    private Set<ContractStage> contractStages;

    public void addCounterpartyContract(CounterpartyContract counterpartyContract) {
        if (this.counterpartyContracts == null) {
            this.counterpartyContracts = new HashSet<>();
        }
        this.counterpartyContracts.add(counterpartyContract);
        counterpartyContract.setContract(this);
    }

    public void addContractStage(ContractStage contractStage) {
        if (this.contractStages == null) {
            this.contractStages = new HashSet<>();
        }
        this.contractStages.add(contractStage);
        contractStage.setContract(this);
    }

}
