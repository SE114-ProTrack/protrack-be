package com.protrack.protrack_be.model.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermissionId implements Serializable {

    @Column(name = "ID_DuAn")
    private Integer projectId;

    @Column(name = "ID_NguoiDung")
    private Integer userId;

    @Column(name = "ID_ChucNang")
    private Integer functionId;
}
