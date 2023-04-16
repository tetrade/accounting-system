package com.accountingsystem.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FilterRequest {

    private EPublicKey key;

    private ETargetEntity targetEntity;

    private EOperator operator;

    private String value;
}
