package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "taikhoan")
public class Account {
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
