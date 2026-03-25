package com.reza.events.domain.hibernate.converter;

import com.reza.events.util.HibernateConstants;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

@Converter
public class YesOrNoTypeConverter implements AttributeConverter<Boolean, String> {

    public static final String YES = HibernateConstants.CONVERTER_YES_OR_NO_YES;
    public static final String NO = HibernateConstants.CONVERTER_YES_OR_NO_NO;
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return BooleanUtils.toString(attribute, YES, NO, NO);
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return BooleanUtils.toBoolean(StringUtils.isNotBlank(dbData) ? dbData : NO, YES, NO);
    }
}
