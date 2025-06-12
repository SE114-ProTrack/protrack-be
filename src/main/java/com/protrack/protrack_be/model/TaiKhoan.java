package com.protrack.protrack_be.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NonNull;

@Entity
@Data
@Table(name = "TAIKHOAN")
public class TaiKhoan {
    @Id
    @Column(name = "ID_TaiKhoan")
    private int accId;

    @Column(name = "Email")
    @NonNull
    private String email;

    @Column(name = "MatKhau")
    @NonNull
    private String password;

    @Column(name = "TrangThai")
    @NonNull
    private boolean status;
}
