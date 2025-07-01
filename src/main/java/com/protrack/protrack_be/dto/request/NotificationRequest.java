package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotNull(message = "ID người nhận không được để trống")
    private UUID receiverId;

    @NotBlank(message = "Loại thông báo không được để trống")
    private String type;

    @NotBlank(message = "Nội dung thông báo không được để trống")
    private String content;

    private String actionUrl;
}
