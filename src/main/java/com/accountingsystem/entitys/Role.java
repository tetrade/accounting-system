package com.accountingsystem.entitys;

import com.accountingsystem.entitys.enums.ERole;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ERole name;
}
