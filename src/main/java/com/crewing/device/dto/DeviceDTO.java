package com.crewing.device.dto;

import com.crewing.device.entity.Device;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DeviceDTO {

    @Getter
    @AllArgsConstructor
    public static class DeviceRegisterRequest {
        @Schema(description = "Fcm 토큰", example = "qwe...")
        private String fcmToken;

        @Schema(description = "핸드폰 모델", example = "IPhone 15 pro")
        private String model;

        @Schema(description = "운영 체제", example = "IOS")
        private String os;

        @Schema(description = "운영 체제 버전", example = "IOS 18")
        private String osVersion;

        @Schema(description = "앱 버전", example = "Crewing 1.0")
        private String appVersion;

        @Schema(description = "코드 푸쉬 버전", example = "Code Push 1.0")
        private String codePushVersion;
    }

    @Getter
    @AllArgsConstructor
    public static class DeviceUpdateRequest {
        @Schema(description = "Fcm 토큰", example = "qwe...")
        private String fcmToken;

        @Schema(description = "운영 체제 버전", example = "IOS 18")
        private String osVersion;

        @Schema(description = "앱 버전", example = "Crewing 1.0")
        private String appVersion;

        @Schema(description = "코드 푸쉬 버전", example = "Code Push 1.0")
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
