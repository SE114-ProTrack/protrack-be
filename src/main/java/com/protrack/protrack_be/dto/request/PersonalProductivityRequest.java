package com.protrack.protrack_be.dto.request;

import com.protrack.protrack_be.validation.CreateGroup;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalProductivityRequest {

    @NotNull(groups = CreateGroup.class, message = "ID người dùng không được để trống")
    private UUID userId;

    @NotNull(groups = CreateGroup.class, message = "ID dự án không được để trống")
    private UUID projectId;

    @NotNull(groups = CreateGroup.class, message = "Số task hoàn thành không được để trống")
    private Integer completedTasks;

}
