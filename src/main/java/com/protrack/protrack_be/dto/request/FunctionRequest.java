package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionRequest {
    @NotBlank(message = "Tên chức năng không được để trống")
    @Size(max = 100)
    private String fuctionName;

    @NotBlank(message = "Tên màn hình không được để trống")
    @Size(max = 100)
    private String screenName;
}