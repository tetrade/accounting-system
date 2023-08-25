package com.accountingsystem.controller.customValidator;

import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class EndDateAfterStartValidator implements ConstraintValidator<EndDateAfterStart, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(EndDateAfterStart constraintAnnotation) {
        startDateField = constraintAnnotation.startDateField();
        endDateField = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate startDate = (LocalDate) getFieldValue(o, startDateField);
            LocalDate endDate = (LocalDate) getFieldValue(o, endDateField);
            return startDate == null || endDate == null || endDate.isEqual(startDate) || endDate.isAfter(startDate);
        } catch (Exception e) {
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field passwordField = ReflectionUtils.findField(clazz, fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }
}
