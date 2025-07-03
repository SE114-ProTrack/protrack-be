package com.protrack.protrack_be.dto.response;

import com.protrack.protrack_be.model.TaskAttachment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private UUID taskId;
    private String taskName;
    private String description;
    private LocalDateTime deadline;
    private String priority;
    private Boolean isMain;
    private List<TaskAttachmentResponse> attachments;
    private UUID projectId;
    private String projectName;

    private List<LabelResponse> labels;

    private UUID parentTaskId;

    private UUID approverId;
    private String approverName;

    private LocalDateTime createdTime;

    private String status;

    private String icon;

    private String color;
}
