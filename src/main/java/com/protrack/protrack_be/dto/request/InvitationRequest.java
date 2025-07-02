package com.protrack.protrack_be.dto.request;

import com.protrack.protrack_be.validation.CreateGroup;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationRequest {

    @NotNull(groups = CreateGroup.class, message = "Mã dự án không được để trống")
    private UUID projectId;

    @NotBlank(groups = CreateGroup.class, message = "Email người được mời không được để trống")
    @Email(message = "Email không hợp lệ")
    private String invitationEmail;

    @NotBlank(groups = CreateGroup.class, message = "Vai trò không được để trống")
    private String role = "Member";
}
