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
@Table(name = "binhluan")
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "id_binhluan", columnDefinition = "BINARY(16)")
    private UUID commentId;

    @ManyToOne
    @JoinColumn(name = "id_congviec", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "id_nguoidung", nullable = false)
    private User user;

    @Column(name = "noidung", nullable = false)
    private String content;

    @Column(name = "thoigianbl")
    private LocalDateTime timestamp;
}
