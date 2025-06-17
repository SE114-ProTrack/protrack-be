package com.protrack.protrack_be.model;

import com.protrack.protrack_be.model.id.ProjectMemberId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "THANHVIENDUAN")
public class ProjectMember {

    @EmbeddedId
    private ProjectMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "ID_DuAn", referencedColumnName = "ID_DuAn", nullable = false)
    private Project projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "ID_NguoiDung", referencedColumnName = "ID_NguoiDung", nullable = false)
    private User userId;

    @Column(name = "LaChuDuAn")
    private Boolean isProjectOwner = false;
}
