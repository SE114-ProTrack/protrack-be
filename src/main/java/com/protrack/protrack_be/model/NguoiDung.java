package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Entity
@Data
@Table(name = "NGUOIDUNG")
public class NguoiDung {
    @Id
    @Column(name = "ID_NguoiDung")
    private int userId;

    @Column(name = "HoTen")
    @NonNull
    private String name;

    @Column(name = "NgaySinh")
    private Date dob;

    @Column(name = "GioiTinh")
    private String gender;

    @Column(name = "DienThoai")
    private String phone;

    @Column(name = "DiaChi")
    private String address;

    @Column(name = "HinhAnh")
    @NonNull
    private String avatar;

    @ManyToOne
    @JoinColumn(name = "ID_TaiKhoan")
    private int accId;
}
