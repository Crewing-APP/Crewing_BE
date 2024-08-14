package com.crewing.applicant.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class ApplicantStatusConverter implements AttributeConverter<Status,String> {
    @Override
    public String convertToDatabaseColumn(Status attribute) {
        if (Objects.isNull(attribute)) {
            throw new NullPointerException("Enum Converting String - OrderStatus is null");
        }
        return attribute.toString();
    }

    @Override
    public Status convertToEntityAttribute(String dbData) {
        if(Objects.isNull(dbData)) {
            throw new NullPointerException("Enum Converting String - OrderStatus is null");
        }
        return Status.valueOf(dbData);
    }
}
