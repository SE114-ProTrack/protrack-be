package com.protrack.protrack_be.model.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberId implements Serializable {

    @Column(name = "id_duan", columnDefinition = "BINARY(16)")
    private UUID projectId;

    @Column(name = "id_nguoidung", columnDefinition = "BINARY(16)")
    private UUID userId;
}
