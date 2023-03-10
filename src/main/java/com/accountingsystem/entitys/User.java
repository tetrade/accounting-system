package com.accountingsystem.entitys;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="full_name")
    private String fullName;

    @Column(name="login", unique = true)
    private String login;

    @Column(name="password")
    private String password;

    @Column(name="terminate_date")
    private LocalDate dateOfTermination;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Contract> contracts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles;

    public void addContract(Contract contract){
        if (this.contracts == null) this.contracts = new HashSet<>();
        this.contracts.add(contract);
        contract.setUser(this);
    }
}
