package com.protrack.protrack_be.model;

import com.protrack.protrack_be.model.id.ThanhVienDuAnId;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "THANHVIENDUAN")
public class ThanhVienDuAn {

    @EmbeddedId
    private ThanhVienDuAnId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "ID_DuAn", referencedColumnName = "ID_DuAn", nullable = false)
    private DuAn projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "ID_NguoiDung", referencedColumnName = "ID_NguoiDung", nullable = false)
    private NguoiDung userId;

    @Column(name = "LaChuDuAn")
    private Boolean isProjectOwner = false;
}
