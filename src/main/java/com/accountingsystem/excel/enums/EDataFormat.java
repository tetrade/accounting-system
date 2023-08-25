package com.accountingsystem.excel.enums;

public enum EDataFormat {
    DATE("DD.MM.YYYY"), CURRENCY("#,##0.00 ₽"), NONE("#");

    private final String format;

    EDataFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }
}
