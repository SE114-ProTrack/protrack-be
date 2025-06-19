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
@Table(name = "THONGBAO")
public class Notification {
    @Id
    @GeneratedValue
    @Column(name = "ID_ThongBao", columnDefinition = "BINARY(16)")
    private UUID notificationId;

    @ManyToOne
    @JoinColumn(name = "ID_NguoiNhan", nullable = false)
    private User receiver;

    @Column(name = "LoaiThongBao", nullable = false)
    private String type;

    @Column(name = "NoiDung", nullable = false)
    private String content;

    @Column(name = "DaDoc")
    private Boolean isRead = false;

    @Column(name = "ThoiGian")
    private LocalDateTime timestamp;
}
