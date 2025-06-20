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
@Table(name = "NGUOITHUCHIENCV")
public class TaskMember {

    @EmbeddedId
    private TaskMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskId")
    @JoinColumn(name = "ID_CongViec", referencedColumnName = "ID_CongViec", nullable = false)
    private Task taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "ID_NguoiDung", referencedColumnName = "ID_NguoiDung", nullable = false)
    private User userId;
}
