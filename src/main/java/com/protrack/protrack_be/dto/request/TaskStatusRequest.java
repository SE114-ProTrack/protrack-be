package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusRequest {
    @NotBlank
    private String status; // TO_DO, IN_PROGRESS, DONE
}