package com.accountingsystem.advice;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

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
