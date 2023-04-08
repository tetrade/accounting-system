package com.accountingsystem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EContractType {
    MAIN("Договор"), COUNTERPARTY_CONTRACT("Договор с контрагентом"), REF_CONTRACT("Связанный контракт");

    private String type;

    EContractType(String type) {
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
