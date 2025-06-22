package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailRequest {

    @NotNull(message = "Mã công việc cha không được để trống")
    private UUID parentTaskId;

    @NotNull(message = "Mã công việc con không được để trống")
    private UUID childTaskId;
}
