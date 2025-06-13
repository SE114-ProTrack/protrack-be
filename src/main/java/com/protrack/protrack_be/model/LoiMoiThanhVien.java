package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOIMOITHANHVIEN")
public class LoiMoiThanhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LoiMoi")
    private Integer invitationId;

    @ManyToOne
    @JoinColumn(name = "ID_DuAn", referencedColumnName = "ID_DuAn", nullable = false)
    private Integer projectId;

    @Column(name = "Email", nullable = false, length = 500)
    private String invitationEmail;

    @Column(name = "Token", nullable = false, length = 500)
    private String invitationToken;

    @Column(name = "NgayMoi")
    @CreationTimestamp
    private LocalDateTime invitationDay;
}
