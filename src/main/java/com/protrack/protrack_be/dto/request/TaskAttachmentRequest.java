package com.protrack.protrack_be.dto.request;

import com.protrack.protrack_be.validation.CreateGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TaskAttachmentRequest {
    @NotNull(groups = CreateGroup.class)
    private UUID taskId;
    private String fileName;
    private String fileUrl;
    private String fileType;
}

