package com.accountingsystem.advice;

import com.accountingsystem.advice.exceptions.IllegalFieldValueException;
import com.accountingsystem.advice.exceptions.NoSuchRowException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(NoSuchRowException.class)
    public ResponseEntity<AppError> catchNoSuchRowException(NoSuchRowException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AppError(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<AppError> catchNoSuchRowException(SQLIntegrityConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AppError(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(IllegalFieldValueException.class)
    public ResponseEntity<AppError> catchNoSuchRowException(IllegalFieldValueException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AppError(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<AppError> catchNoSuchRowException(InvalidFormatException ex) {
       StringBuilder s = new StringBuilder("Invalid `"+ ex.getPath().get(ex.getPath().size() - 1).getFieldName() +"` value.");
        if (ex.getValue().toString().equals("")) s.append(" Can`t be empty");
        else s.append(" Possible values: ").append(ex.getOriginalMessage().split("(\\[)|(\\])")[1]);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AppError(HttpStatus.BAD_REQUEST.value(), s.toString()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AppError> catchE(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AppError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
}
