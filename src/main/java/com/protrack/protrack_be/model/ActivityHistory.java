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
@Table(name = "lichsuhoatdong")
public class ActivityHistory {
    @Id
    @GeneratedValue
    @Column(name = "id_lshd", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_nguoidung", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_congviec", nullable = false)
    private Task task;

    @Column(name = "loaihanhdong", nullable = false)
    private String actionType;

    @Column(name = "mota")
    private String description;

    @Column(name = "thoigian")
    private LocalDateTime timestamp;
}
