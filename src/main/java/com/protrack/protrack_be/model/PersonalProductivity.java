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
@Table(name = "HIEUSUATCANHAN")
public class PersonalProductivity {

    @EmbeddedId
    private PersonalProductivityId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "ID_NguoiDung", referencedColumnName = "ID_NguoiDung", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "ID_DuAn", referencedColumnName = "ID_DuAn", nullable = false)
    private Project project;

    @Column(name = "SoTaskHoanThanh", nullable = false)
    private Integer completedTasks;

    @Column(name = "NgayCapNhat", nullable = false)
    private LocalDateTime lastUpdated;
}
