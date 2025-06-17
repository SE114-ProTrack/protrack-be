package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TAIKHOAN")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TaiKhoan")
    private UUID accId;

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
