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
@SQLDelete(sql = "UPDATE loimoithanhvien SET da_xoa = true WHERE id_loimoi = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loimoithanhvien")
public class Invitation extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id_loimoi", columnDefinition = "BINARY(16)")
    private UUID invitationId;

    @ManyToOne
    @JoinColumn(name = "id_duan", referencedColumnName = "id_duan", nullable = false)
    private Project project;

    @Column(name = "email", nullable = false, length = 500)
    private String invitationEmail;

    @Column(name = "token", nullable = false, length = 500)
    private String invitationToken;

    @Column(name = "ngaymoi")
    @CreationTimestamp
    private LocalDateTime invitationDate;

    @Column(name = "dachapnhan")
    private boolean accepted;
}
