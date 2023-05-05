package com.accountingsystem.controller.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContractStageDto {
    private int id;

    @NotBlank
    private String name;

    private BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate actualStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate actualEndDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotBlank
    @Pattern(regexp = "^\\d{1,2}-\\d{1,2}-\\d{4}$", message = "дата должна быть формата: dd-MM-yyyy")
    private LocalDate plannedStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotBlank
    @Pattern(regexp = "^\\d{1,2}-\\d{1,2}-\\d{4}$", message = "дата должна быть формата: dd-MM-yyyy")
    private LocalDate plannedEndDate;

    @Pattern(regexp = "\\d+(/.?\\d*)?", message = "число должно быть формата: dd.DD")
    private BigDecimal actualMaterialCosts;

    @Pattern(regexp = "\\d+(/.?\\d*)?", message = "число должно быть формата: dd.DD")
    private BigDecimal plannedMaterialCosts;

    @Pattern(regexp = "\\d+(/.?\\d*)?", message = "число должно быть формата: dd.DD")
    private BigDecimal actualSalaryExpenses;

    @Pattern(regexp = "\\d+(/.?\\d*)?", message = "число должно быть формата: dd.DD")
    private BigDecimal plannedSalaryExpenses;
}
