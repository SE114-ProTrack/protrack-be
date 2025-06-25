package com.protrack.protrack_be.model;

import com.protrack.protrack_be.model.id.PersonalProductivityId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hieusuatcanhan")
public class PersonalProductivity {

    @EmbeddedId
    private PersonalProductivityId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "id_nguoidung", referencedColumnName = "id_nguoidung", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "id_duan", referencedColumnName = "id_duan", nullable = false)
    private Project project;

    @Column(name = "sotaskhoanthanh", nullable = false)
    private Integer completedTasks;

    @Column(name = "ngaycapnhat", nullable = false)
    private LocalDateTime lastUpdated;
}
