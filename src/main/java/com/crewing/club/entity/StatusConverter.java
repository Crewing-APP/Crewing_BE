package com.crewing.club.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status,String> {
    @Override
    public String convertToDatabaseColumn(Status attribute) {
        if (Objects.isNull(attribute)) {
            throw new NullPointerException("Enum Converting String - OrderStatus is null");
        }
        return attribute.toString();
    }

    @Override
    public Status convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData) || dbData.isEmpty()) {
            return null;
        }
        for (Status status : Status.values()) {
            if (status.getKey().equals(dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status value: " + dbData);
    }
}
