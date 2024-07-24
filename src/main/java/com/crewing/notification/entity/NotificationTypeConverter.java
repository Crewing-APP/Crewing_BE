package com.crewing.notification.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class NotificationTypeConverter implements AttributeConverter<NotificationType, String> {
    @Override
    public String convertToDatabaseColumn(NotificationType attribute) {
        if (Objects.isNull(attribute)) {
            throw new NullPointerException("Enum Converting String - OrderStatus is null");
        }

        return attribute.toString();
    }

    @Override
    public NotificationType convertToEntityAttribute(String dbData) {
        return NotificationType.valueOf(dbData);
    }
}
