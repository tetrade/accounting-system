package com.accountingsystem.repository;

import com.accountingsystem.entitys.ContractStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ContractStageRepo extends
        JpaRepository<ContractStage, Integer>, JpaSpecificationExecutor<ContractStage> {

    @Query(value="FROM ContractStage cs LEFT JOIN FETCH cs.contract c LEFT JOIN FETCH c.user u" +
            " LEFT JOIN FETCH u.roles r WHERE u.login = :user_login AND c.id = :contract_id")
    Set<ContractStage> getContractStagesByContractIdAndUserLogin(
            @Param("user_login") String login, @Param("contract_id") Integer id
    );

    @Modifying
    @Query("delete from ContractStage cs where cs.id = :id")
    void deleteById(@Param("id") int id);
}
