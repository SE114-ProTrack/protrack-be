package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tokenresetmatkhau")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "id_taikhoan", nullable = false)
    private Account account;

    @Column(name = "hethan", nullable = false)
    private Instant expiredAt;

    @Column(name = "daxacthuc", nullable = false)
    private boolean verified = false;
}

