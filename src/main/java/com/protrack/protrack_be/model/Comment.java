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
@SQLDelete(sql = "UPDATE binhluan SET da_xoa = true WHERE id_binhluan = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "binhluan")
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "id_binhluan", columnDefinition = "BINARY(16)")
    private UUID commentId;

    @ManyToOne
    @JoinColumn(name = "id_congviec", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "id_nguoidung", nullable = false)
    private User user;

    @Column(name = "noidung", nullable = false)
    private String content;
}
