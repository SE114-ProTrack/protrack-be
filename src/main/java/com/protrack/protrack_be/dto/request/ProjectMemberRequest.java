package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberRequest {

    @NotNull(message = "Mã dự án không được để trống")
    private Integer projectId;

    @NotNull(message = "Mã người dùng không được để trống")
    private Integer userId;

    private Boolean isProjectOwner = false;
}
