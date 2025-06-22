package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CONGVIEC")
public class ActivityHistory {
    @Id
    @GeneratedValue
    @Column(name = "ID_LSHD", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ID_NguoiDung", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ID_CongViec", nullable = false)
    private Task task;

    @Column(name = "LoaiHanhDong", nullable = false)
    private String actionType;

    @Column(name = "MoTa")
    private String description;

    @Column(name = "ThoiGian")
    private LocalDateTime timestamp;
}
