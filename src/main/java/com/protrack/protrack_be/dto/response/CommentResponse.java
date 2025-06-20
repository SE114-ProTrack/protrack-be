package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private UUID commentId;

    private UUID taskId;
    private UUID userId;
    private String userFullName;

    private String content;
    private LocalDateTime timestamp;
}
