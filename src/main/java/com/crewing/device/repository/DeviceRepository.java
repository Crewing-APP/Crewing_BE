package com.crewing.device.repository;

import com.crewing.device.entity.Device;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findAllByUserId(Long userId);

    Optional<Device> findByIdAndUserId(Long deviceId, Long userId);

    void deleteAllByUserId(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Device d SET d.user = null WHERE d.user.id in :userIds")
    void updateDeviceUserToNullByUserIdsIn(List<Long> userIds);
}
