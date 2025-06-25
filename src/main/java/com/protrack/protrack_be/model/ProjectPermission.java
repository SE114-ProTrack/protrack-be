package com.protrack.protrack_be.model;

import com.protrack.protrack_be.model.id.ProjectPermissionId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PHANQUYENDUAN")
public class ProjectPermission {

    @EmbeddedId
    private ProjectPermissionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "id_duan", referencedColumnName = "id_duan", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "id_nguoidung", referencedColumnName = "id_nguoidung", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("functionId")
    @JoinColumn(name = "id_chucnang", referencedColumnName = "id_chucnang", nullable = false)
    private Function function;

    @Column(name = "trangthai")
    private Boolean isActive = false;
}
