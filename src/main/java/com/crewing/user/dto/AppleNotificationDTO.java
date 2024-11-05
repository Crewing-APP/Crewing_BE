package com.crewing.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AppleNotificationDTO {
    @JsonProperty("status")
    private String status; // e.g., "success" or "failure"

    @JsonProperty("message")
    private String message; // Description of the response

    @JsonProperty("data")
    private NotificationData data; // Nested object for additional data

    @JsonProperty("timestamp")
    private Long timestamp; // Timestamp of when the notification was sent

    // Inner class to hold specific notification data
    @Getter
    public static class NotificationData {
        @JsonProperty("notificationId")
        private String notificationId; // Unique identifier for the notification

        @JsonProperty("eventType")
        private String eventType; // Type of the event that triggered the notification

        @JsonProperty("payload")
        private Object payload; // The actual content of the notification (could be different types)
    }
}
