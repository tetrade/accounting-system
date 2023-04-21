package com.accountingsystem.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {

    private EPublicKey key;

    private ETargetEntity targetEntity;

    private EOperator operator;

    private String value;
}
