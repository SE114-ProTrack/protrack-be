package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private UUID messageId;
    private String content;
    private LocalDateTime timestamp;

    private UUID projectId;
    private String projectName;

    private UUID senderId;
    private String senderFullName;
}
