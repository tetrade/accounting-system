package com.accountingsystem.entitys.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EType {
    PURCHASE("Закупка"), DELIVERY("Поставка"), WORKS("Работы");

    private final String type;

    EType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return this.type;
    }
}
