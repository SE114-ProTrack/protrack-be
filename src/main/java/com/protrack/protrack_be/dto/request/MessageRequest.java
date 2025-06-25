package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    @NotNull(message = "ID người nhận không được để trống")
    private UUID receiverId;

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    private String content;
}
