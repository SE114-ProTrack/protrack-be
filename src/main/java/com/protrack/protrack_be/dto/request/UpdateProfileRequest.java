package com.protrack.protrack_be.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileRequest {
    private String name;
    private LocalDate dob;
    private String gender;
    private String phone;
    private String address;
    private String avatarUrl;
}
