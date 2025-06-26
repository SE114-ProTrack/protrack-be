package com.protrack.protrack_be.model;

import com.protrack.protrack_be.model.id.TaskMemberId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nguoithuchiencv")
public class TaskMember {

    @EmbeddedId
    private TaskMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskId")
    @JoinColumn(name = "id_congviec", referencedColumnName = "id_congviec", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "id_nguoidung", referencedColumnName = "id_nguoidung", nullable = false)
    private User user;
}
