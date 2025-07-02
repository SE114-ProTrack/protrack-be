package com.protrack.protrack_be.model;

import com.protrack.protrack_be.model.id.ProjectMemberId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@EqualsAndHashCode(callSuper = false)
@Entity
@SQLDelete(sql = "UPDATE thanhvienduan SET da_xoa = true WHERE id_duan = ? AND id_nguoidung = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "thanhvienduan")
public class ProjectMember extends BaseEntity {

    @EmbeddedId
    private ProjectMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "id_duan", referencedColumnName = "id_duan", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "id_nguoidung", referencedColumnName = "id_nguoidung", nullable = false)
    private User user;

    @Column(name = "lachuduan")
    private Boolean isProjectOwner = false;

    @Column(name = "vaitro")
    private String role = "Member";
}
