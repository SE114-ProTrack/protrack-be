package com.protrack.protrack_be.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "congviec")
public class Task {
    @Id
    @GeneratedValue
    @Column(name = "id_congviec", columnDefinition = "BINARY(16)")
    private UUID taskId;

    @ManyToOne
    @JoinColumn(name = "id_duan", nullable = false)
    private Project project;

    @Column(name = "tencongviec", nullable = false)
    private String taskName;

    @Column(name = "mota")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_nhan")
    private Label label;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "isprojectmaintask")
    private Boolean isMain = true;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @ToString.Exclude
    private List<TaskAttachment> attachment;

    @ManyToOne
    @JoinColumn(name = "congvieccha")
    private Task parentTask;

    @ManyToOne
    @JoinColumn(name = "id_nguoiduyet", nullable = false)
    private User approver;

    @Column(name = "mucdouutien")
    private String priority;

    @Column(name = "thoigiantao")
    private LocalDateTime createdTime;

    @Column(name = "trangthai")
    private String status; // TO_DO, IN_PROGRESS, DONE

    @Column(name = "icon")
    private String icon;

    @Column(name = "ma_mau")
    private String color;
}
