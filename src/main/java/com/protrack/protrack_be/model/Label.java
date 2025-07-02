package com.protrack.protrack_be.model;

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
@SQLDelete(sql = "UPDATE nhancongviec SET da_xoa = true WHERE id_nhan = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nhancongviec")
public class Label extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id_nhan", columnDefinition = "BINARY(16)")
    private UUID labelId;

    @ManyToOne
    @JoinColumn(name = "id_duan", nullable = false)
    private Project project;

    @Column(name = "tennhan", nullable = false)
    private String labelName;

    @Column(name = "mau", nullable = false)
    private String color;

    @Column(name = "mota")
    private String description;
}
