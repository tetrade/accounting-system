package com.accountingsystem.filters;


import com.accountingsystem.advice.exceptions.IllegalFieldValueException;
import com.accountingsystem.entitys.TypeConverter;
import com.accountingsystem.entitys.enums.EType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Stream;

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
            try { return LocalDate.parse(value, formatter); }
            catch (Exception ex) {throw new IllegalFieldValueException("Illegal `value` field. Wrong date format. Should be: dd-MM-yyyy"); }
        }
    },
    DECIMAL {
        @Override
        public Object getValue(String value) {
            try { return new BigDecimal(value); }
            catch (NumberFormatException ex) { throw new IllegalFieldValueException("Illegal `value` field. Wrong number format"); }
        }
    }, INTEGER {
        @Override
        public Object getValue(String value) {
            if (Objects.equals(value, "null")) return null;
            try { return Integer.valueOf(value); }
            catch (NumberFormatException ex) { throw new IllegalFieldValueException("Illegal `value` field. Wrong number Format"); }
        }
    }, TYPE {
        @Override
        public Object getValue(String value) {
            try {
                return new TypeConverter().convertToEntityAttribute(value);
            } catch (IllegalArgumentException ex) {
                throw new IllegalFieldValueException("Illegal `value` field. Should be: " + Stream.of(EType.values()).map(EType::getType)
                        .reduce((x, y) -> x + ", " + y).orElse(""));
            }
        }
    };


    public abstract Object getValue(String value);
}
