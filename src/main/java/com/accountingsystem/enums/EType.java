package com.accountingsystem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EType {
    PURCHASE("Закупка"), DELIVERY("Поставка"), WORKS("Работы");

    private String type;

    EType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return this.type;
    }

    public void setType(String realType) {
        this.type = realType;
    }
}
