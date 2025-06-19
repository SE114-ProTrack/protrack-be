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
@Table(name = "BINHLUAN")
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "ID_BinhLuan", columnDefinition = "BINARY(16)")
    private UUID commentId;

    @ManyToOne
    @JoinColumn(name = "ID_CongViec", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "ID_NguoiDung", nullable = false)
    private User user;

    @Column(name = "NoiDung", nullable = false)
    private String content;

    @Column(name = "ThoiGianBL")
    private LocalDateTime timestamp;
}
