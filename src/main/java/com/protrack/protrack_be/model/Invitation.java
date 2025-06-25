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
@Table(name = "loimoithanhvien")
public class Invitation {
    @Id
    @GeneratedValue
    @Column(name = "id_loimoi", columnDefinition = "BINARY(16)")
    private UUID invitationId;

    @ManyToOne
    @JoinColumn(name = "id_duan", referencedColumnName = "id_duan", nullable = false)
    private Project project;

    @Column(name = "email", nullable = false, length = 500)
    private String invitationEmail;

    @Column(name = "token", nullable = false, length = 500)
    private String invitationToken;

    @Column(name = "ngaymoi")
    @CreationTimestamp
    private LocalDateTime invitationDate;

    @Column(name = "dachapnhan")
    private boolean accepted;
}
