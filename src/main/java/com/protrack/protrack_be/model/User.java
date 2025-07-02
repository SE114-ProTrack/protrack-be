package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@SQLDelete(sql = "UPDATE task SET da_xoa = true WHERE id_congviec = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "nguoidung")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
