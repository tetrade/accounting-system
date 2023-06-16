package com.accountingsystem.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {

    @NotNull
    private EPublicKey key;

    @NotNull
    private ETargetEntity targetEntity;

    @NotNull
    private EOperator operator;

    @NotNull
    private String value;
}
