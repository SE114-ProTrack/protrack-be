package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskMemberRequest {

    @NotNull(message = "ID công việc không được để trống")
    private UUID taskId;

    @NotNull(message = "ID người dùng không được để trống")
    private UUID userId;
}
