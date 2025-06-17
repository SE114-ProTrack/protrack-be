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
@Table(name = "NGUOIDUNG")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NguoiDung")
    private UUID userId;

    @Column(name = "HoTen")
    @NonNull
    private String name;

    @Column(name = "NgaySinh")
    private LocalDate dob;

    @Column(name = "GioiTinh")
    private String gender;

    @Column(name = "DienThoai")
    private String phone;

    @Column(name = "DiaChi")
    private String address;

    @Column(name = "HinhAnh")
    @NonNull
    private String avatarUrl;

    @ManyToOne
    @JoinColumn(name = "ID_TaiKhoan")
    private Account account;
}
