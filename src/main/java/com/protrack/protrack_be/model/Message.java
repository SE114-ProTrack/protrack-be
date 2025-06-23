package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TINNHAN")
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "ID_TinNhan", columnDefinition = "BINARY(16)")
    private UUID messageId;

    @ManyToOne
    @JoinColumn(name = "ID_DuAn", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "ID_NguoiGui", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "ID_NguoiNhan", nullable = false)
    private User receiver;

    @Column(name = "NoiDung", nullable = false)
    private String content;

    @Column(name = "ThoiGianGui")
    private LocalDateTime timestamp;

    @Column(name = "DaDoc")
    private boolean read = false;

    @Column(name = "ThoiGianDoc")
    private LocalDateTime readAt;
}
