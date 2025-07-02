package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@SQLDelete(sql = "UPDATE duan SET da_xoa = true WHERE id_duan = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "duan")
public class Project extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id_duan", columnDefinition = "BINARY(16)")
    private UUID projectId;

    @ManyToOne
    @JoinColumn(name = "id_nguoidung", referencedColumnName = "id_nguoidung", nullable = false)
    private User creatorId;

    @Column(name = "tenduan", nullable = false, length = 100)
    private String projectName;

    @Column(name = "mota", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "banner")
    private String bannerUrl;
}
