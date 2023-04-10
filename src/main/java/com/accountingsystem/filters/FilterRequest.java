package com.accountingsystem.filters;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FilterRequest {

    private EPublicKey key;

    private ETargetEntity targetEntity;

    private EOperator operator;

    private String value;
}
