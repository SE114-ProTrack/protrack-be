package com.protrack.protrack_be.dto.request;

import com.protrack.protrack_be.validation.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotNull(groups = CreateGroup.class, message = "ID người nhận không được để trống")
    private UUID receiverId;

    @NotBlank(groups = CreateGroup.class, message = "Loại thông báo không được để trống")
    private String type;

    @NotBlank(groups = CreateGroup.class, message = "Nội dung thông báo không được để trống")
    private String content;

    private String actionUrl;
}
