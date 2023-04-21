package com.accountingsystem.advice.exceptions;

public class IllegalFieldValueException extends IllegalArgumentException{

    public static final String KEY_ERROR = "Illegal `key` field value for ";

    public static final String TARGET_ENTITY_ERROR = "Illegal `targetEntity` field value for ";

    public IllegalFieldValueException(String message) { super(message);}
}
