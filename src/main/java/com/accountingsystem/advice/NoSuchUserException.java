package com.accountingsystem.advice;

public class NoSuchUserException extends RuntimeException{
    public NoSuchUserException(String message) { super(message); }
}
