package com.accountingsystem.entitys;

import com.accountingsystem.entitys.enums.EType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class TypeConverter implements AttributeConverter<EType, String> {

    @Override
    public String convertToDatabaseColumn(EType eType) {
        if (eType == null) return null;
        return eType.getType();

    }

    @Override
    public EType convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return Stream.of(EType.values())
                .filter(c -> c.getType().equals(s))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
