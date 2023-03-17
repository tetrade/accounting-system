package com.accountingsystem.repository;

import com.accountingsystem.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    boolean existsByLogin(String login);
}
