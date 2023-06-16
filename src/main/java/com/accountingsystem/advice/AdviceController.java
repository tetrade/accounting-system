package com.accountingsystem.advice;

import com.accountingsystem.advice.exceptions.IllegalFieldValueException;
import com.accountingsystem.advice.exceptions.NoSuchRowException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(NoSuchRowException.class)
    public ResponseEntity<AppError> catchNoSuchRowException(NoSuchRowException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AppError(ex.getMessage()));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<AppError> catchNoSuchRowException(SQLIntegrityConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AppError(ex.getMessage()));
    }

    @ExceptionHandler(IllegalFieldValueException.class)
    public ResponseEntity<AppError> catchNoSuchRowException(IllegalFieldValueException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AppError(ex.getMessage()));
    }

    @ExceptionHandler(InvalidFormatException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<Violation> catchNoSuchRowException(InvalidFormatException ex) {
        Violation v = new Violation();
        v.setFieldName(ex.getPath().get(ex.getPath().size() - 1).getFieldName());
        if (ex.getValue().toString().equals("")) v.setMessage("не может быть пустым");
        else if (ex.getTargetType().isEnum()) v.setMessage("возможные значения: " + ex.getOriginalMessage().split("(\\[)|(\\])")[1]);
        else if (ex.getTargetType().equals(BigDecimal.class)) v.setMessage("неправильный числовой формат");
        else if (ex.getTargetType().equals(LocalDate.class)) v.setMessage("неправильный формат даты, должен быть dd-MM-yyyy");
        else v.setMessage(ex.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(v);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<? extends AppError> catchE(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getMostSpecificCause();
        if (rootCause instanceof InvalidFormatException || rootCause instanceof DateTimeParseException)
            return catchNoSuchRowException((InvalidFormatException) ex.getCause());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AppError(ex.getMostSpecificCause().getLocalizedMessage().split("\n")[0]));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> onConstraintValidationException(
            ConstraintViolationException e
    ) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationErrorResponse(violations));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationErrorResponse(violations));
    }

}
