package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityHistoryRequest {

    @NotNull(message = "ID người dùng không được để trống")
    private UUID userId;

    @NotNull(message = "ID công việc không được để trống")
    private UUID taskId;

    @NotBlank(message = "Loại hành động không được để trống")
    private String actionType;

    private String description;
}
