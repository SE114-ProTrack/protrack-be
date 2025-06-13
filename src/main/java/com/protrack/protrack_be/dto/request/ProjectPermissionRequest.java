package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermissionRequest {

    @NotNull(message = "Mã dự án không được để trống")
    private Long projectId;

    @NotNull(message = "Mã người dùng không được để trống")
    private Long userId;

    @NotNull(message = "Mã chức năng không được để trống")
    private Long functionId;

    private Boolean isActive = false;
}
