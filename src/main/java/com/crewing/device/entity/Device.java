package com.crewing.device.entity;

import com.crewing.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String fcmToken;

    @Column
    private String model;

    @Column
    private String os;

    @Column
    private String osVersion;

    @Column
    private String appVersion;

    @Column
    private String codePushVersion;

    @Builder
    public Device(User user, String fcmToken, String model, String os, String osVersion, String appVersion,
                  String codePushVersion) {
        this.user = user;
        this.fcmToken = fcmToken;
        this.model = model;
        this.os = os;
        this.osVersion = osVersion;
        this.appVersion = appVersion;
        this.codePushVersion = codePushVersion;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void updateOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void updateAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void updateCodePushVersion(String codePushVersion) {
        this.codePushVersion = codePushVersion;
    }
}
