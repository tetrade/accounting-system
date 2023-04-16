package com.accountingsystem.filters;
import com.accountingsystem.advice.exceptions.IllegalFieldValueException;
import com.accountingsystem.entitys.*;

import javax.persistence.criteria.*;

public enum ETargetEntity {
    COUNTERPARTY_ORGANIZATION("counterpartyOrganization") {
        @Override
        public Expression<?> createExpression(Root<?> sourceRoot, String key){
            Class<?> sourceRootClass = sourceRoot.getJavaType();
            Path<?> p;
            if (sourceRootClass == CounterpartyOrganization.class) p = sourceRoot;
            else if (sourceRootClass == Contract.class)
                p = join(
                        join(sourceRoot, COUNTERPARTY_CONTRACT.getName(), JoinType.INNER),
                        getName(), JoinType.INNER
                );
            else if (sourceRootClass == CounterpartyContract.class) {
                p = join(sourceRoot, getName(), JoinType.INNER);
            } else throw new IllegalFieldValueException("Illegal `targetEntity` field value");

            try { p = p.get(key); }
            catch (IllegalArgumentException e) { throw new IllegalFieldValueException("Illegal `key` field value"); }

            return p;
        }
    }, COUNTERPARTY_CONTRACT("counterpartyContracts") {
        @Override
        public Expression<?> createExpression(Root<?> sourceRoot, String key) {
            Class<?> sourceRootClass = sourceRoot.getJavaType();
            Path<?> p;
            if (sourceRootClass == CounterpartyContract.class) p = sourceRoot;
            else if (sourceRootClass == Contract.class) p =  join(sourceRoot, getName(), JoinType.INNER);
            else throw new IllegalFieldValueException("Illegal `targetEntity` field value");

            try { p = p.get(key);}
            catch (IllegalArgumentException e) { throw new IllegalFieldValueException("Illegal `key` field value"); }

            return p;
        }
    },
    CONTRACT("contract") {
        @Override
        public Expression<?> createExpression(Root<?> sourceRoot, String key) {
            Class<?> sourceRootClass = sourceRoot.getJavaType();
            Path<?> p;


            if (sourceRootClass == Contract.class) p = sourceRoot;
            else if (sourceRootClass == CounterpartyContract.class || sourceRootClass == ContractStage.class) {
                p = join(sourceRoot, getName(), JoinType.INNER);
            } else throw new IllegalFieldValueException("Illegal `targetEntity` field value");

            try { p = p.get(key); }
            catch (IllegalArgumentException e) { throw new IllegalFieldValueException("Illegal `key` field value"); }

            return p;
        }
    },
    CONTRACT_STAGE("contractStages") {
        @Override
        public Expression<?> createExpression(Root<?> sourceRoot, String key) {
            Class<?> sourceRootClass = sourceRoot.getJavaType();
            Path<?> p;

            if (sourceRootClass == ContractStage.class) p = sourceRoot;
            else if (sourceRootClass == Contract.class) p = join(sourceRoot, getName(), JoinType.INNER);
            else throw new IllegalFieldValueException("Illegal `targetEntity` field value");

            try { p = p.get(key); }
            catch (IllegalArgumentException e) { throw new IllegalFieldValueException("Illegal `key` field value"); }

            return p;
        }
    },
    USER("user") {
        @Override
        public Expression<?> createExpression(Root<?> sourceRoot, String key) {
            Class<?> sourceRootClass = sourceRoot.getJavaType();
            Path<?> p;

            if (sourceRootClass == User.class) p = sourceRoot;
            else if (sourceRootClass == Contract.class) p = join(sourceRoot, getName(), JoinType.INNER);
            else if (sourceRootClass == CounterpartyContract.class || sourceRootClass == ContractStage.class)
                p = join(
                        join(sourceRoot, CONTRACT.getName(), JoinType.INNER),
                        getName(), JoinType.INNER
                );
            else throw new IllegalFieldValueException("Illegal `targetEntity` field value");

            try { p = p.get(key); }
            catch (IllegalArgumentException e) { throw new IllegalFieldValueException("Illegal `key` field value"); }

            return p;
        }
    };

    private final String name;

    ETargetEntity(String name) {
        this.name = name;
    }

    public abstract Expression<?> createExpression(Root<?> root, String key);

    public String getName() {return name;}

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
