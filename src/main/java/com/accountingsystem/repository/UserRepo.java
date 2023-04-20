package com.accountingsystem.repository;

import com.accountingsystem.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepo extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    boolean existsByLogin(String login);


}
