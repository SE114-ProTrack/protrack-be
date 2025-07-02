package com.protrack.protrack_be.dto.request;

import com.protrack.protrack_be.validation.CreateGroup;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskMemberRequest {

    @NotNull(groups = CreateGroup.class, message = "ID công việc không được để trống")
    private UUID taskId;

    @NotNull(groups = CreateGroup.class, message = "ID người dùng không được để trống")
    private UUID userId;
}
