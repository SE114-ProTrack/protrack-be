package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOIMOITHANHVIEN")
public class Invitation {
    @Id
    @GeneratedValue
    @Column(name = "ID_LoiMoi", columnDefinition = "BINARY(16)")
    private UUID invitationId;

    @ManyToOne
    @JoinColumn(name = "ID_DuAn", referencedColumnName = "ID_DuAn", nullable = false)
    private Project project;

    @Column(name = "Email", nullable = false, length = 500)
    private String invitationEmail;

    @Column(name = "Token", nullable = false, length = 500)
    private String invitationToken;

    @Column(name = "NgayMoi")
    @CreationTimestamp
    private LocalDateTime invitationDate;

    @Column(name = "DaChapNhan")
    private boolean accepted;
}
