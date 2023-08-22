package com.accountingsystem.repository;

import com.accountingsystem.entitys.Role;
import com.accountingsystem.entitys.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole role);
}
