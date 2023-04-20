package com.accountingsystem.filters;


import com.accountingsystem.entitys.TypeConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
            if (Objects.equals(value, "null")) return null;
            return Integer.valueOf(value);
        }
    }, TYPE {
        @Override
        public Object getValue(String value) { return new TypeConverter().convertToEntityAttribute(value); }
    };


    public abstract Object getValue(String value);
}
