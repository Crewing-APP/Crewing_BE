package com.crewing.device.api;

import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.device.dto.DeviceDTO.DeviceInfoResponse;
import com.crewing.device.dto.DeviceDTO.DeviceRegisterRequest;
import com.crewing.device.dto.DeviceDTO.DeviceUpdateRequest;
import com.crewing.device.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "디바이스" , description = "디바이스 정보를 조회 및 관리 합니다")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/devices")
public class DeviceController {
    private final DeviceService deviceService;

    @Operation(summary = "나의 디바이스 정보 조회",description = "나의 디바이스 정보를 조회합니다")
    @GetMapping
    public ResponseEntity<List<DeviceInfoResponse>> getDeviceInfoByLoginUser(@AuthenticationPrincipal PrincipalDetails principalDetails){
        return ResponseEntity.ok(deviceService.getDevicesInfo(principalDetails.getId()));
    }

    @Operation(summary = "나의 디바이스 정보 등록",description = "나의 디바이스 정보를 등록합니다")
    @PostMapping
    public ResponseEntity<DeviceInfoResponse> registryDevice(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                             DeviceRegisterRequest request){
        return ResponseEntity.ok(deviceService.registryDevice(request,principalDetails.getId()));
    }

    @Operation(summary = "디바이스 정보 갱신",description = "나의 디바이스 정보를 갱신합니다")
    @PatchMapping("/{deviceId}")
    public ResponseEntity<DeviceInfoResponse> updateDevice(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                           DeviceUpdateRequest request,
                                                           @PathVariable Long deviceId){
        return ResponseEntity.ok(deviceService.updateDevice(request,deviceId,principalDetails.getId()));
    }
}
