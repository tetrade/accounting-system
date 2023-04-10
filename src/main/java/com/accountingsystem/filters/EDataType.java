package com.accountingsystem.filters;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum EDataType {

    STRING {
        @Override
        public Object getValue(String value){
            return value;
        }
    },
    DATA {
        @Override
        public Object getValue(String value) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(value, formatter);
        }
    },
    DECIMAL {
        @Override
        public Object getValue(String value) {
            return new BigDecimal(value);
        }
    }, INTEGER {
        @Override
        public Object getValue(String value) {
            return Integer.valueOf(value);
        }
    };


    public abstract Object getValue(String value);
}
