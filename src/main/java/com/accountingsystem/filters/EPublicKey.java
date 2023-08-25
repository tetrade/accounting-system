package com.accountingsystem.filters;

public enum EPublicKey {

    NAME("name", EDataType.STRING), AMOUNT("amount", EDataType.DECIMAL),
    TYPE("type", EDataType.TYPE), ID("id", EDataType.INTEGER),
    LOGIN("login", EDataType.STRING), FULL_NAME("fullName", EDataType.STRING),
    INN("inn", EDataType.STRING), ADDRESS("address", EDataType.STRING), USER("user", EDataType.INTEGER),
    ACTUAL_START_DATE("actualStartDate", EDataType.DATA),
    ACTUAL_END_DATE("actualEndDate", EDataType.DATA),
    PLANNED_START_DATE("plannedStartDate", EDataType.DATA),
    PLANNED_END_DATE("plannedEndDate", EDataType.DATA),
    ACTUAL_MATERIAL_COSTS("actualMaterialCosts", EDataType.DECIMAL),
    PLANNED_MATERIAL_COSTS("plannedMaterialCosts", EDataType.DECIMAL),
    ACTUAL_SALARY_EXPENSES("actualSalaryExpenses", EDataType.DECIMAL),
    PLANNED_SALARY_EXPENSES("plannedSalaryExpenses", EDataType.DECIMAL);

    private final String name;
    private final EDataType type;

    EPublicKey(String name, EDataType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public EDataType getType() {
        return type;
    }
}
