package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.NotificationResponse;
import com.protrack.protrack_be.model.Notification;
import com.protrack.protrack_be.model.User;

public class NotificationMapper {

    public static NotificationResponse toResponse(Notification entity) {
        return new NotificationResponse(
                entity.getNotificationId(),
                entity.getSender().getAvatarUrl(),
                entity.getSender().getName(),
                entity.getReceiver().getUserId(),
                entity.getReceiver().getName(),
                entity.getType(),
                entity.getContent(),
                entity.getIsRead(),
                entity.getCreatedAt(),
                entity.getActionUrl()
        );
    }
}
