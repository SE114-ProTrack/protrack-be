package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@SQLDelete(sql = "UPDATE taikhoan SET da_xoa = true WHERE id_taikhoan = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "taikhoan")
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id_taikhoan", columnDefinition = "uuid")
    private UUID accId;

    @Column(name = "email")
    @NonNull
    private String email;

    @Column(name = "matkhau")
    @NonNull
    private String password;

    @Column(name = "dahoatdong")
    private boolean active;
}
