package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokenxacthucemail")
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "id_taikhoan", referencedColumnName = "id_taikhoan", nullable = false)
    private Account account;

    @Column(name = "hethan", nullable = false)
    private Instant expiredAt;

    @Column(name = "daxacthuc", nullable = false)
    private boolean verified = false;

    public EmailVerificationToken(String token, Account acc, Instant instant) {
        this.token = token;
        this.account = acc;
        this.expiredAt = instant;
        this.verified = false;
    }
}
