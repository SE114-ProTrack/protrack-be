package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tinnhan")
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "id_tinnhan", columnDefinition = "BINARY(16)")
    private UUID messageId;

    @ManyToOne
    @JoinColumn(name = "id_nguoigui", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "id_nguoinhan", nullable = false)
    private User receiver;

    @Column(name = "noidung", nullable = false)
    private String content;

    @Column(name = "thoigiangui")
    private LocalDateTime sentAt;

    @Column(name = "thoigiansua")
    private LocalDateTime updatedAt;

    @Column(name = "dadoc")
    private boolean read = false;

    @Column(name = "thoigiandoc")
    private LocalDateTime readAt;
}
