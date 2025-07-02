package com.protrack.protrack_be.dto.request;

import com.protrack.protrack_be.validation.CreateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {
    @NotBlank(groups = CreateGroup.class, message = "Tên dự án không được để trống")
    @Size(max = 100)
    private String projectName;

    private String description;

    @Schema(hidden = true)
    private String bannerUrl;
}