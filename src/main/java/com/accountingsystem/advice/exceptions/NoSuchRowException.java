package com.accountingsystem.advice.exceptions;

import java.util.NoSuchElementException;

public class NoSuchRowException extends NoSuchElementException {
    public NoSuchRowException(String columnName, Object value, String entity){
        super(String.format("%s with such %s %s doesn't exist", entity, columnName, value));
    }
}
