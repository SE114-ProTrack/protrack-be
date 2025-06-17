package com.protrack.protrack_be.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    private String name;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải ở trong quá khứ")
    private LocalDate dob;

    @NotBlank(message = "Giới tính không được để trống")
    private String gender;

    private String phone;
    private String address;
}
