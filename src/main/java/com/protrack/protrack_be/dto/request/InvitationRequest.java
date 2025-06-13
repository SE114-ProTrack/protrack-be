package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationRequest {

    @NotNull(message = "Mã dự án không được để trống")
    private Integer projectId;

    @NotBlank(message = "Email người được mời không được để trống")
    @Email(message = "Email không hợp lệ")
    private String invitationEmail;
}
