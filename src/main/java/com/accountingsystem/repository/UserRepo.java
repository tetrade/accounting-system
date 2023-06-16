package com.accountingsystem.repository;

import com.accountingsystem.entitys.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepo extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    boolean existsByLogin(String login);

    @EntityGraph(value = "User.roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findByLogin(String login);

    @EntityGraph(value = "User.roles", type = EntityGraph.EntityGraphType.FETCH)
    Page<User> findAll(Specification<User> specification, Pageable pageable);

    @EntityGraph(value = "User.roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findById(Integer id);

    @Modifying
    @Query(value = "delete from \"user\" u where u.id = :id", nativeQuery = true, countQuery = "select 1")
    void deleteById(@Param("id") Integer id);
}
