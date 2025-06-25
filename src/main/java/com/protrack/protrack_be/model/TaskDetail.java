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
@Table(name = "chitietcongviec")
public class TaskDetail {

    @EmbeddedId
    private TaskDetailId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("parentTaskId")
    @JoinColumn(name = "id_cvcha", referencedColumnName = "id_cvcha", nullable = false)
    private Task parentTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("childTaskId")
    @JoinColumn(name = "id_cvcon", referencedColumnName = "id_cvcon", nullable = false)
    private Task childTask;
}
