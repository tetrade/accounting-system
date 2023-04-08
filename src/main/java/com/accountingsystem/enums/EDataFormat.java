package com.accountingsystem.enums;

public enum EDataFormat {
    DATE("dd-mm-yyyy"), CURRENCY("#,##0.00 ₽"), NONE("#");

    private final String format;

    EDataFormat(String format) {
        this.format = format;
    }

    public String getFormat() { return this.format; }
}
