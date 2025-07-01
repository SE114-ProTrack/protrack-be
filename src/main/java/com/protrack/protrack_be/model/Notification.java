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
@Table(name = "thongbao")
public class Notification {
    @Id
    @GeneratedValue
    @Column(name = "id_thongbao", columnDefinition = "BINARY(16)")
    private UUID notificationId;

    @ManyToOne
    @JoinColumn(name = "id_nguoinhan", nullable = false)
    private User receiver;

    @Column(name = "loaithongbao", nullable = false)
    private String type;

    @Column(name = "noidung", nullable = false)
    private String content;

    @Column(name = "dadoc")
    private Boolean isRead = false;

    @Column(name = "thoigian")
    private LocalDateTime timestamp;

    @Column(name = "duongdan")
    private String actionUrl;
}
