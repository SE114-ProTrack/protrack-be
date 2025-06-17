package com.protrack.protrack_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID userId;
    private String name;
    private String email;
    private LocalDate dob;
    private String gender;
    private String phone;
    private String address;
    private String avatarUrl;
}
