package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelRequest {

    @NotNull(message = "Mã dự án không được để trống")
    private UUID projectId;

    @NotBlank(message = "Tên nhãn không được để trống")
    private String labelName;

    private String description;
}
