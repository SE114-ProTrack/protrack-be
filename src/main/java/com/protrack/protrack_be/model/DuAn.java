package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DUAN")
public class DuAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DuAn")
    private Integer projectId;

    @ManyToOne
    @JoinColumn(name = "ID_NguoiDung", referencedColumnName = "ID_NguoiDung", nullable = false)
    private Integer creatorId;

    @Column(name = "TenDuAn", nullable = false, length = 100)
    private String projectName;

    @Column(name = "ThoiGianTao")
    @CreationTimestamp
    private LocalDateTime createTime;

    @Column(name = "MoTa", columnDefinition = "NVARCHAR(MAX)")
    private String description;
}
