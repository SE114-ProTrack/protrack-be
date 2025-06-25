package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteProfileRequest {
    private UUID accountId;

    @NotBlank(message = "Họ tên không được để trống")
    private String name;

    private String address;

    @NotBlank(message = "Giới tính không được để trống")
    private String gender;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải ở trong quá khứ")
    private LocalDate dob;

    private String phone;
}

