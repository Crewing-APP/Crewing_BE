package com.crewing.device.service;

import com.crewing.common.error.device.DeviceNotFoundException;
import com.crewing.device.dto.DeviceDTO.DeviceInfoResponse;
import com.crewing.device.dto.DeviceDTO.DeviceRegisterRequest;
import com.crewing.device.dto.DeviceDTO.DeviceTokensResponse;
import com.crewing.device.dto.DeviceDTO.DeviceUpdateRequest;
import com.crewing.device.entity.Device;
import com.crewing.device.repository.DeviceRepository;
import com.crewing.user.entity.User;
import com.crewing.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final UserService userService;

    public List<DeviceInfoResponse> getDevicesInfo(Long userId) {
        return deviceRepository.findAllByUserId(userId).stream().map(
                DeviceInfoResponse::toDTO
        ).toList();
    }

    public DeviceInfoResponse registryDevice(DeviceRegisterRequest request, Long userId) {
        User user = userService.getUserById(userId);

        Device device = Device.builder()
                .fcmToken(request.getFcmToken())
                .model(request.getModel())
                .os(request.getOs())
                .osVersion(request.getOsVersion())
                .appVersion(request.getAppVersion())
                .codePushVersion(request.getCodePushVersion())
                .user(user)
                .build();

        return DeviceInfoResponse.toDTO(deviceRepository.save(device));
    }

    public DeviceInfoResponse updateDevice(DeviceUpdateRequest request, Long deviceId, Long userId) {
        Device device = deviceRepository.findByIdAndUserId(deviceId, userId).orElseThrow(DeviceNotFoundException::new);

        device.updateFcmToken(request.getFcmToken());
        device.updateOsVersion(request.getOsVersion());
        device.updateAppVersion(request.getAppVersion());
        device.updateCodePushVersion(request.getCodePushVersion());

        return DeviceInfoResponse.toDTO(deviceRepository.save(device));
    }

    public void removeDevice(Long deviceId, Long userId) {
        Device device = deviceRepository.findByIdAndUserId(deviceId, userId).orElseThrow(DeviceNotFoundException::new);

        deviceRepository.delete(device);
    }

    public DeviceTokensResponse getFcmTokensByUserIds(List<Long> userIds){
        List<String> tokens =
                userIds.stream().map(id -> deviceRepository.findAllByUserId(id)
                .stream().map(Device::getFcmToken).toList()
        ).flatMap(List::stream).toList();

        return DeviceTokensResponse.builder()
                .fcmTokens(tokens)
                .build();
    }
}
