package com.protrack.protrack_be.dto.request;

import com.protrack.protrack_be.validation.CreateGroup;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionRequest {
    @NotBlank(groups = CreateGroup.class, message = "Code chức năng không được để trống")
    private String functionCode;

    @NotBlank(groups = CreateGroup.class, message = "Tên chức năng không được để trống")
    @Size(max = 100)
    private String fuctionName;

    @NotBlank(groups = CreateGroup.class, message = "Tên màn hình không được để trống")
    @Size(max = 100)
    private String screenName;
}