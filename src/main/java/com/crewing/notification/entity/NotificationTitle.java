package com.crewing.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class NotificationTitle {
    @Column(nullable = false)
    private String title;

    public NotificationTitle(String title){
        this.title = title;
    }
}
