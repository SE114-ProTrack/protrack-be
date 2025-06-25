package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotNull(message = "ID công việc không được để trống")
    private UUID taskId;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;
}
