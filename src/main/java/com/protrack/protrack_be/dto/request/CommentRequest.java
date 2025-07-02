package com.protrack.protrack_be.dto.request;

import com.protrack.protrack_be.validation.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotNull(groups = CreateGroup.class, message = "ID công việc không được để trống")
    private UUID taskId;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;
}
