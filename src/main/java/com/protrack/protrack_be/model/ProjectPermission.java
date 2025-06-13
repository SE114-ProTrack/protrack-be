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
    @JoinColumn(name = "ID_DuAn", referencedColumnName = "ID_DuAn", nullable = false)
    private Project projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "ID_NguoiDung", referencedColumnName = "ID_NguoiDung", nullable = false)
    private NguoiDung userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("functionId")
    @JoinColumn(name = "ID_ChucNang", referencedColumnName = "ID_ChucNang", nullable = false)
    private Function functionId;

    @Column(name = "TrangThai")
    private Boolean isActive = false;
}
