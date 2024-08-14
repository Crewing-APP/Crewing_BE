package com.crewing.member.entity;

import com.crewing.notification.entity.NotificationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {
    @Override
    public String convertToDatabaseColumn(Role attribute) {
        if (Objects.isNull(attribute)) {
            throw new NullPointerException("Enum Converting String - OrderStatus is null");
        }
        return attribute.toString();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        if(Objects.isNull(dbData)) {
            throw new NullPointerException("Enum Converting String - OrderStatus is null");
        }
        return Role.valueOf(dbData);
    }
}
