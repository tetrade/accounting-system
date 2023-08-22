package com.accountingsystem.excel.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EContractType {
    MAIN("Договор"), COUNTERPARTY_CONTRACT("Договор с контрагентом"), REF_CONTRACT("Связанный контракт");

    private final String type;

    EContractType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return this.type;
    }
}
