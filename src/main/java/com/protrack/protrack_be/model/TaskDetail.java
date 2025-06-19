package com.protrack.protrack_be.model;

import com.protrack.protrack_be.model.id.TaskDetailId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CHITIETCONGVIEC")
public class TaskDetail {

    @EmbeddedId
    private TaskDetailId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("parentTaskId")
    @JoinColumn(name = "ID_CVCha", referencedColumnName = "ID_CVCha", nullable = false)
    private Task parentTaskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("childTaskId")
    @JoinColumn(name = "ID_CVCon", referencedColumnName = "ID_CVCon", nullable = false)
    private Task childTaskId;
}
