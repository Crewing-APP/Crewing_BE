package com.crewing.device.repository;

import com.crewing.device.entity.Device;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findAllByUserId(Long userId);

    Optional<Device> findByIdAndUserId(Long deviceId, Long userId);
}
