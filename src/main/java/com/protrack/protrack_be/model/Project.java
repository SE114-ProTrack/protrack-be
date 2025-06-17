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
@Table(name = "DUAN")
public class Project {
    @Id
    @GeneratedValue
    @Column(name = "ID_DuAn", columnDefinition = "BINARY(16)")
    private UUID projectId;

    @ManyToOne
    @JoinColumn(name = "ID_NguoiDung", referencedColumnName = "ID_NguoiDung", nullable = false)
    private User creatorId;

    @Column(name = "TenDuAn", nullable = false, length = 100)
    private String projectName;

    @Column(name = "ThoiGianTao")
    @CreationTimestamp
    private LocalDateTime createTime;

    @Column(name = "MoTa", columnDefinition = "NVARCHAR(MAX)")
    private String description;
}
