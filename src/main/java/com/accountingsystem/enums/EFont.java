package com.accountingsystem.enums;


import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public enum EFont {
    TIMES_NEW_ROMAN {
        public XSSFFont getFont(XSSFWorkbook workbook, int fontSize) {
            XSSFFont font = workbook.createFont();
            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short)fontSize);
            return font;
        }
    };
    public abstract XSSFFont getFont(XSSFWorkbook workbook, int fontSize);
}
