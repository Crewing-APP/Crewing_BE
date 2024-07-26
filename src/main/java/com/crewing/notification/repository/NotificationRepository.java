package com.crewing.notification.repository;

import com.crewing.notification.entity.Notification;
import com.crewing.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByReceiver(User receiver, Pageable pageable);

    void deleteAllByReceiverId(Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE Notification n WHERE n.receiver.id in :receiverIds")
    int deleteAllByReceiverIdsIn(List<Long> receiverIds);
}
