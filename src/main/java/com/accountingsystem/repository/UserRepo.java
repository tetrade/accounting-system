package com.accountingsystem.repository;

import com.accountingsystem.entitys.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    boolean existsByLogin(String login);

    @EntityGraph(value = "User.roles", type = EntityGraph.EntityGraphType.FETCH)
    Page<User> findAll(Specification<User> specification, Pageable pageable);

    @Modifying
    @Query("delete from User u where u.id = :id")
    void deleteById(@Param("id") int id);
}
