package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberRequest {

    @NotNull(message = "Mã dự án không được để trống")
    private UUID projectId;

    @NotNull(message = "Mã người dùng không được để trống")
    private UUID userId;

    private Boolean isProjectOwner = false;
    private String role = "Member";
}
