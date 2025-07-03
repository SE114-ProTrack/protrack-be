package com.protrack.protrack_be.dto.request;

import com.protrack.protrack_be.validation.CreateGroup;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    @NotNull(groups = CreateGroup.class, message = "ID dự án không được để trống")
    private UUID projectId;

    @NotBlank(groups = CreateGroup.class, message = "Tên công việc không được để trống")
    private String taskName;

    private String description;

    private List<UUID> labelIds;

    @NotNull(groups = CreateGroup.class, message = "Deadline không được để trống")
    private LocalDateTime deadline;

    private Boolean isMain = true;

    private List<TaskAttachmentRequest> attachments;

    private UUID parentTaskId;

    @NotNull(groups = CreateGroup.class, message = "Người duyệt không được để trống")
    private UUID approverId;

    private String priority;

    private String status;

    private String icon;

    private String color;

    private List<UUID> assigneeIds;
}
