package com.accountingsystem.entitys;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "counterparty_organization")
@Entity
@Data
@EqualsAndHashCode(exclude = {
        "counterpartyContracts"
})
public class CounterpartyOrganization{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id"  )
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="address")
    private String address;

    @Column(name="inn", unique = true, nullable = false, columnDefinition = "char")
    private String inn;

    @OneToMany(
            cascade = {
                    CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH
            }, mappedBy = "counterpartyOrganization", fetch = FetchType.LAZY
    )
    private Set<CounterpartyContract> counterpartyContracts;

    public void addCounterpartyContract(CounterpartyContract counterpartyContract) {
        if (this.counterpartyContracts == null) {
            counterpartyContracts = new HashSet<>();
        }
        this.counterpartyContracts.add(counterpartyContract);
        counterpartyContract.setCounterpartyOrganization(this);
    }

    public void removeCounterpartyContract(CounterpartyContract counterpartyContract) {
        this.counterpartyContracts.remove(counterpartyContract);
        counterpartyContract.setCounterpartyOrganization(null);
    }
}
