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
public class TaskMemberId implements Serializable {

    @Column(name = "id_congviec", columnDefinition = "BINARY(16)")
    private UUID taskId;

    @Column(name = "id_nguoidung", columnDefinition = "BINARY(16)")
    private UUID userId;
}
