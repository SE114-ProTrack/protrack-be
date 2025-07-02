package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@SQLDelete(sql = "UPDATE lichsuhoatdong SET da_xoa = true WHERE id_lshd = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lichsuhoatdong")
public class ActivityHistory extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id_lshd", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_nguoidung", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_congviec", nullable = false)
    private Task task;

    @Column(name = "loaihanhdong", nullable = false)
    private String actionType;

    @Column(name = "mota")
    private String description;
}
