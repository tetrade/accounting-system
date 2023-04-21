package com.accountingsystem.repository;

import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.entitys.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepo extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    boolean existsByLogin(String login);

    @EntityGraph(value = "User.roles", type = EntityGraph.EntityGraphType.FETCH)
    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
