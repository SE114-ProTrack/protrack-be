package com.protrack.protrack_be.model;

import com.protrack.protrack_be.model.id.TaskMemberId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@SQLDelete(sql = "UPDATE nguoithuchiencv SET da_xoa = true WHERE id_congviec = ? AND id_nguoidung = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nguoithuchiencv")
public class TaskMember extends BaseEntity {

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
