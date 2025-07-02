package com.protrack.protrack_be.dto.request;

import com.protrack.protrack_be.validation.CreateGroup;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermissionRequest {

    @NotNull(groups = CreateGroup.class, message = "Mã dự án không được để trống")
    private UUID projectId;

    @NotNull(groups = CreateGroup.class, message = "Mã người dùng không được để trống")
    private UUID userId;

    @NotNull(groups = CreateGroup.class, message = "Mã chức năng không được để trống")
    private UUID functionId;

    private Boolean isActive = false;
}
