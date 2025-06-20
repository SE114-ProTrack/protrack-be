package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalProductivityRequest {

    @NotNull(message = "ID người dùng không được để trống")
    private UUID userId;

    @NotNull(message = "ID dự án không được để trống")
    private UUID projectId;

    @NotNull(message = "Số task hoàn thành không được để trống")
    private Integer completedTasks;

    @NotNull(message = "Ngày cập nhật không được để trống")
    private LocalDateTime lastUpdated;
}
