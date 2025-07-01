package com.protrack.protrack_be.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class TaskAttachmentRequest {
    private UUID taskId;
    private String fileName;
    private String fileUrl;
    private String fileType;
}

