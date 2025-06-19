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
public class Task {
    @Id
    @GeneratedValue
    @Column(name = "ID_CongViec", columnDefinition = "BINARY(16)")
    private UUID taskId;

    @ManyToOne
    @JoinColumn(name = "ID_DuAn", nullable = false)
    private Project project;

    @Column(name = "TenCongViec", nullable = false)
    private String taskName;

    @Column(name = "MoTa")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ID_Nhan")
    private Label label;

    @Column(name = "Deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "IsProjectMainTask")
    private Boolean isMain = true;

    @Column(name = "TepDinhKem")
    private String attachment;

    @ManyToOne
    @JoinColumn(name = "CongViecLienQuan")
    private Task relatedTask;

    @ManyToOne
    @JoinColumn(name = "ID_NguoiDuyet", nullable = false)
    private User approver;

    @Column(name = "MucDoUuTien")
    private String priority;
}
