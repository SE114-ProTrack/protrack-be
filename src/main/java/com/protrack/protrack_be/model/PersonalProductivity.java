package com.protrack.protrack_be.model;

import com.protrack.protrack_be.model.id.PersonalProductivityId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.UUID;
@EqualsAndHashCode(callSuper = false)
@Entity
@SQLDelete(sql = "UPDATE hieusuatcanhan SET da_xoa = true WHERE id_nguoidung = ? AND id_duan = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hieusuatcanhan")
public class PersonalProductivity extends BaseEntity {

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
}
