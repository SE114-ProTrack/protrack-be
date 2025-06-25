package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "nguoidung")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nguoidung")
    private UUID userId;

    @Column(name = "hoten")
    @NonNull
    private String name;

    @Column(name = "ngaysinh")
    private LocalDate dob;

    @Column(name = "gioitinh")
    private String gender;

    @Column(name = "dienthoai")
    private String phone;

    @Column(name = "diachi")
    private String address;

    @Column(name = "hinhanh")
    @NonNull
    private String avatarUrl;

    @ManyToOne
    @JoinColumn(name = "id_taikhoan")
    private Account account;
}
