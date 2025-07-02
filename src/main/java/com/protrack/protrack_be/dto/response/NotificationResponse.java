package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private UUID notificationId;

    private String senderAvt;
    private String senderName;

    private UUID receiverId;
    private String receiverFullName;

    private String type;
    private String content;
    private Boolean isRead;
    private LocalDateTime timestamp;

    private String actionUrl;
}
