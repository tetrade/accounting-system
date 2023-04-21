package com.accountingsystem.excel.enums;

public enum EColumn {

    CURRENT_NUMBER("№", 256 * 6) , TYPE_MAIN("Тип договора", 256 * 16) ,
    NAME("Название", 256 * 30), TYPE("Цель договора", 256 * 16),
    AMOUNT("Сумма",256 * 27) , PLANNED_START_DATE("Плановая дата начала", 256 * 20),
    PLANNED_END_DATE("Плановая дата окончания", 256 * 20),
    ACTUAL_START_DATE("Фактическая дата окончания", 256 * 20),
    ACTUAL_END_DATE("Фактическая дата начала", 256 * 20),
    RELATED_CONTRACT("Основной контракт", 256 * 16),
    PLANNED_MATERIAL_COSTS("Плановые расходы на материалы", 256 * 28),
    ACTUAL_MATERIAL_COSTS("Фактические расходы на материалы", 256 * 28),
    PLANNED_SALARY_EXPENSES("Плановые расходы на зарплату", 256 * 28),
    ACTUAL_SALARY_EXPENSES("Фактические расходы на зарплату", 256 * 28);

    private final String name;
    private final Integer columnWidth;

    EColumn(String name, Integer columnWidth) {
        this.name = name;
        this.columnWidth = columnWidth;

    }

    public String getName() {
        return name;
    }
    public Integer getColumnWidth() { return columnWidth; }
}
