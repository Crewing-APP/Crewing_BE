package com.crewing.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class NotificationMessage {
    @Column(nullable = false)
    private String message;

    public NotificationMessage(String message){
        this.message = message;
    }
}
