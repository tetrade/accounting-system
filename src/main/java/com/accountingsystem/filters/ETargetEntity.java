package com.accountingsystem.filters;

import com.accountingsystem.advice.exceptions.IllegalFieldValueException;
import com.accountingsystem.entitys.Contract;
import com.accountingsystem.entitys.ContractStage;
import com.accountingsystem.entitys.CounterpartyContract;
import com.accountingsystem.entitys.CounterpartyOrganization;
import com.accountingsystem.entitys.User;


import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public enum ETargetEntity {

    COUNTERPARTY_ORGANIZATION("counterpartyOrganization") {
        @Override
        public Expression<?> createExpression(Root<?> sourceRoot, String key) {
            Class<?> sourceRootClass = sourceRoot.getJavaType();
            Path<?> p;
            if (sourceRootClass == CounterpartyOrganization.class) {
                p = sourceRoot;
            } else if (sourceRootClass == Contract.class) {
                p = join(
                        join(sourceRoot, COUNTERPARTY_CONTRACT.getName(), JoinType.INNER),
                        getName(), JoinType.INNER
                );
            } else if (sourceRootClass == CounterpartyContract.class) {
                p = join(sourceRoot, getName(), JoinType.INNER);
            } else
                throw new IllegalFieldValueException(IllegalFieldValueException.TARGET_ENTITY_ERROR + sourceRoot.getJavaType().getName());

            try {
                p = p.get(key);
            } catch (IllegalArgumentException e) {
                throw new IllegalFieldValueException(IllegalFieldValueException.KEY_ERROR + this.name());
            }

            return p;
        }
    }, COUNTERPARTY_CONTRACT("counterpartyContracts") {
        @Override
        public Expression<?> createExpression(Root<?> sourceRoot, String key) {
            Class<?> sourceRootClass = sourceRoot.getJavaType();
            Path<?> p;
            if (sourceRootClass == CounterpartyContract.class) {
                p = sourceRoot;
            } else if (sourceRootClass == Contract.class) {
                p = join(sourceRoot, getName(), JoinType.INNER);
            } else {
                throw new IllegalFieldValueException(IllegalFieldValueException.TARGET_ENTITY_ERROR + sourceRoot.getJavaType().getName());
            }

            try {
                p = p.get(key);
            } catch (IllegalArgumentException e) {
                throw new IllegalFieldValueException(IllegalFieldValueException.KEY_ERROR + this.name());
            }

            return p;
        }
    },
    CONTRACT("contract") {
        @Override
        public Expression<?> createExpression(Root<?> sourceRoot, String key) {
            Class<?> sourceRootClass = sourceRoot.getJavaType();
            Path<?> p;


            if (sourceRootClass == Contract.class) {
                p = sourceRoot;
            } else if (sourceRootClass == CounterpartyContract.class || sourceRootClass == ContractStage.class) {
                p = join(sourceRoot, getName(), JoinType.INNER);
            } else {
                throw new IllegalFieldValueException(IllegalFieldValueException.TARGET_ENTITY_ERROR + sourceRoot.getJavaType().getName());
            }

            try {
                p = p.get(key);
            } catch (IllegalArgumentException e) {
                throw new IllegalFieldValueException(IllegalFieldValueException.KEY_ERROR + this.name());
            }

            return p;
        }
    },
    CONTRACT_STAGE("contractStages") {
        @Override
        public Expression<?> createExpression(Root<?> sourceRoot, String key) {
            Class<?> sourceRootClass = sourceRoot.getJavaType();
            Path<?> p;

            if (sourceRootClass == ContractStage.class) {
                p = sourceRoot;
            } else if (sourceRootClass == Contract.class) {
                p = join(sourceRoot, getName(), JoinType.INNER);
            } else {
                throw new IllegalFieldValueException(IllegalFieldValueException.TARGET_ENTITY_ERROR + sourceRoot.getJavaType().getName());
            }

            try {
                p = p.get(key);
            } catch (IllegalArgumentException e) {
                throw new IllegalFieldValueException(IllegalFieldValueException.KEY_ERROR + this.name());
            }

            return p;
        }
    },
    USER("user") {
        @Override
        public Expression<?> createExpression(Root<?> sourceRoot, String key) {
            Class<?> sourceRootClass = sourceRoot.getJavaType();
            Path<?> p;

            if (sourceRootClass == User.class) {
                p = sourceRoot;
            } else if (sourceRootClass == Contract.class) {
                p = join(sourceRoot, getName(), JoinType.INNER);
            } else if (sourceRootClass == CounterpartyContract.class || sourceRootClass == ContractStage.class) {
                p = join(
                        join(sourceRoot, CONTRACT.getName(), JoinType.INNER),
                        getName(), JoinType.INNER
                );
            } else {
                throw new IllegalFieldValueException(IllegalFieldValueException.TARGET_ENTITY_ERROR + sourceRoot.getJavaType().getName());
            }

            try {
                p = p.get(key);
            } catch (IllegalArgumentException e) {
                throw new IllegalFieldValueException(IllegalFieldValueException.KEY_ERROR + this.name());
            }

            return p;
        }
    };

    private final String name;

    ETargetEntity(String name) {
        this.name = name;
    }

    public abstract Expression<?> createExpression(Root<?> root, String key);

    public String getName() {
        return name;
    }

    public Join<?, ?> join(From<?, ?> criteriaRoot,
                           String s,
                           JoinType joinType
    ) {
        return criteriaRoot.getJoins().stream()
                .filter(j -> j.getAttribute().getName().equals(s) && j.getJoinType().equals(joinType))
                .findFirst()
                .orElseGet(() -> criteriaRoot.join(s, joinType));
    }
}
