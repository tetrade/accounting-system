package com.accountingsystem.advice;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic=true)
public class Violation extends AppError {
    private String fieldName;

    public Violation(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }
}
