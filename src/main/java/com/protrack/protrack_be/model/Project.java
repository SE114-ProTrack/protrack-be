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
@Table(name = "duan")
public class Project {
    @Id
    @GeneratedValue
    @Column(name = "id_duan", columnDefinition = "BINARY(16)")
    private UUID projectId;

    @ManyToOne
    @JoinColumn(name = "id_nguoidung", referencedColumnName = "id_nguoidung", nullable = false)
    private User creatorId;

    @Column(name = "tenduan", nullable = false, length = 100)
    private String projectName;

    @Column(name = "thoigiantao")
    @CreationTimestamp
    private LocalDateTime createTime;

    @Column(name = "mota", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "banner")
    private String bannerUrl;
}
