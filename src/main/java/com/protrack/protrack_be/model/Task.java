package com.protrack.protrack_be.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.protrack.protrack_be.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@SQLDelete(sql = "UPDATE task SET da_xoa = true WHERE id_congviec = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "congviec")
public class Task extends BaseEntity {
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

    @Column(name = "id_nguoitao")
    private UUID creator;

    @ManyToMany
    @JoinTable(
            name = "gannhancongviec",
            joinColumns = @JoinColumn(name = "id_congviec"),
            inverseJoinColumns = @JoinColumn(name = "id_nhan")
    )
    private List<Label> labels;

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

    @Column(name = "trangthai")
    @Enumerated(EnumType.STRING)
    private TaskStatus status; // TO_DO, IN_PROGRESS, DONE

    @Column(name = "icon")
    private String icon;

    @Column(name = "ma_mau")
    private String color;
}
