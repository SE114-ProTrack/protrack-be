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
public class TaskDetailId implements Serializable {

    @Column(name = "ID_CVCha", columnDefinition = "BINARY(16)")
    private UUID parentTaskId;

    @Column(name = "ID_CVCon", columnDefinition = "BINARY(16)")
    private UUID childTaskId;
}
