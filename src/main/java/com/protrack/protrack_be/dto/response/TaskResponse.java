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
    private List<TaskAttachment> attachments;
    private UUID projectId;
    private String projectName;

    private UUID labelId;
    private String labelName;

    private UUID parentTaskId;

    private UUID approverId;
    private String approverName;

    private String status;

    private String icon;

    private String color;
}
