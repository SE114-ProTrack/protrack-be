package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.NotificationResponse;
import com.protrack.protrack_be.model.Notification;

public class NotificationMapper {

    public static NotificationResponse toResponse(Notification entity) {
        return new NotificationResponse(
                entity.getNotificationId(),
                entity.getReceiver().getUserId(),
                entity.getReceiver().getName(),
                entity.getType(),
                entity.getContent(),
                entity.getIsRead(),
                entity.getTimestamp(),
                entity.getActionUrl()
        );
    }
}
