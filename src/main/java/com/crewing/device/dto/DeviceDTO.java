package com.crewing.device.dto;

import com.crewing.device.entity.Device;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DeviceDTO {

    @Getter
    @AllArgsConstructor
    public static class DeviceRegisterRequest {
        private String fcmToken;
        private String model;
        private String os;
        private String osVersion;
        private String appVersion;
        private String codePushVersion;
    }

    @Getter
    @AllArgsConstructor
    public static class DeviceUpdateRequest {
        private String fcmToken;
        private String osVersion;
        private String appVersion;
        private String codePushVersion;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DeviceInfoResponse {
        private Long id;
        private String fcmToken;
        private String model;
        private String os;
        private String osVersion;
        private String appVersion;
        private String codePushVersion;

        public static DeviceInfoResponse toDTO(Device device) {
            return new DeviceInfoResponse(device.getId(), device.getFcmToken(), device.getModel(),
                    device.getOs(), device.getOsVersion(),
                    device.getAppVersion(), device.getCodePushVersion());
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class DeviceTokensResponse {
        private List<String> fcmTokens;
    }


}
