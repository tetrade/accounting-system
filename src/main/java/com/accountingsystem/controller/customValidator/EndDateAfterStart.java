package com.accountingsystem.controller.customValidator;


import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EndDateAfterStartValidator.class})
@Repeatable(EndDatesAfterStarts.class)
public @interface EndDateAfterStart {
    String message() default "Дата начала должна быть обязательно до даты окончания";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String endDateField();

    String startDateField();

}
