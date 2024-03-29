package com.accountingsystem.filters;

import com.accountingsystem.advice.exceptions.IllegalFieldValueException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;

public enum EOperator {

    EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getKey().getType().getValue(request.getValue());
            Expression<?> key = request.getTargetEntity().createExpression(root, request.getKey().getName());
            if (request.getValue().equalsIgnoreCase("null")) return cb.and(key.isNull(), predicate);
            return cb.and(cb.equal(key, value), predicate);
        }
    },

    NOT_EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getKey().getType().getValue(request.getValue());
            Expression<?> key = request.getTargetEntity().createExpression(root, request.getKey().getName());
            return cb.and(cb.notEqual(key, value), predicate);
        }
    },

    LIKE {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Expression<String> key = (Expression<String>) request.getTargetEntity().createExpression(root, request.getKey().getName());
            return cb.and(cb.like(cb.upper(key), "%" + request.getValue().toUpperCase() + "%"), predicate);
        }
    }, GREATER {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getKey().getType().getValue(request.getValue());
            Expression<?> key = request.getTargetEntity().createExpression(root, request.getKey().getName());
            if (request.getKey().getType().equals(EDataType.DATA)) {
                return cb.and(cb.greaterThanOrEqualTo((Expression<LocalDate>) key, (LocalDate) value), predicate);
            } else if (request.getKey().getType().equals(EDataType.DECIMAL)) {
                return cb.and(cb.greaterThanOrEqualTo((Expression<BigDecimal>) key, (BigDecimal) value), predicate);
            } else throw new IllegalFieldValueException("Illegal `key` field " + request.getKey() + " for " + this.name() + " `operator`");
        }
    } ,
    LESS {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getKey().getType().getValue(request.getValue());
            Expression<?> key = request.getTargetEntity().createExpression(root, request.getKey().getName());
            if (request.getKey().getType().equals(EDataType.DATA)) {
                return cb.and(cb.lessThanOrEqualTo((Expression<LocalDate>) key, (LocalDate) value), predicate);
            } else if (request.getKey().getType().equals(EDataType.DECIMAL)) {
                return cb.and(cb.lessThanOrEqualTo((Expression<BigDecimal>) key, (BigDecimal) value), predicate);
            } else throw new IllegalFieldValueException("Illegal `key` field " + request.getKey() + " for " + this.name() + " `operator`");
        }
    };


    public abstract <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate);
}
