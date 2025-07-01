package com.protrack.protrack_be.dto.request;

import lombok.Data;

@Data
public class TaskAttachmentRequest {
    private String fileName;
    private String fileUrl;
    private String fileType;
}

