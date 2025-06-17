package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {
    @NotBlank(message = "Tên dự án không được để trống")
    @Size(max = 100)
    private String projectName;

    private String description;
}