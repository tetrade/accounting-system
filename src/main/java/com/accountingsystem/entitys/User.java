package com.accountingsystem.entitys;

import lombok.Data;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NamedEntityGraph(
        name = "User.roles",
        attributeNodes = {
                @NamedAttributeNode("roles"),
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "terminate_date")
    private LocalDate terminationDate;

    @OneToMany(cascade = {
            CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH
    },
            mappedBy = "user")
    private Set<Contract> contracts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public void addContract(Contract contract) {
        if (this.contracts == null) {
            this.contracts = new HashSet<>();
        }
        this.contracts.add(contract);
        contract.setUser(this);
    }

    @PreRemove
    public void preRemove() {
        for (Contract c : contracts) {
            c.setUser(null);
        }
    }
}
