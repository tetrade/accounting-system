package com.accountingsystem.controller.dtos;

import com.accountingsystem.controller.customValidator.EndDateAfterStart;
import com.accountingsystem.entitys.enums.EType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.aspectj.lang.annotation.Before;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
@EndDateAfterStart(endDateField = "plannedEndDate", startDateField = "plannedStartDate")
@EndDateAfterStart(endDateField = "actualEndDate", startDateField = "actualStartDate")
abstract class AbstractContract {

    @NotBlank
    private String name;

    @NotNull
    private EType type;

    @DecimalMin(value = "0.0")
    @NotNull
    private BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @NotNull
    private LocalDate plannedStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @NotNull
    private LocalDate plannedEndDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate actualStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate actualEndDate;
}
