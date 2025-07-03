package com.protrack.protrack_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagePreviewResponse {
    private UUID userId;
    private String partnerName;
    private String avatarUrl;
    private String messageContent;
    private LocalDateTime sentAt;
    private int unreadCount;
}

