package com.protrack.protrack_be.dto.request;

import com.protrack.protrack_be.validation.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelRequest {

    @NotNull(groups = CreateGroup.class, message = "Mã dự án không được để trống")
    private UUID projectId;

    @NotBlank(groups = CreateGroup.class, message = "Tên nhãn không được để trống")
    private String labelName;

    @NotBlank(groups = CreateGroup.class, message = "Màu nhãn không được để trống")
    private String color;

    private String description;
}
