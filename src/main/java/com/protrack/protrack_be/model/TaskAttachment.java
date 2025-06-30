package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tepdinhkemcongviec")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAttachment {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_congviec", nullable = false)
    private Task task;

    @Column(name = "tenfile")
    private String fileName;

    @Column(name = "url", nullable = false)
    private String fileUrl; //Upload hoặc link ngoài

    @Column(name = "loaifile")
    private String fileType; //"image/png", "application/pdf", "google-doc"

    @Column(name = "thoigiantai")
    private LocalDateTime uploadedAt;
}

