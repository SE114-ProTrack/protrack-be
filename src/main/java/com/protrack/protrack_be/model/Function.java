package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@SQLDelete(sql = "UPDATE chucnang SET da_xoa = true WHERE id_chucnang = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chucnang")
public class Function extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id_chucnang", columnDefinition = "BINARY(16)")
    private UUID functionId;

    @Column(name = "functioncode", nullable = false, unique = true, length = 100)
    private String functionCode;

    @Column(name = "tenchucnang", nullable = false, length = 100)
    private String functionName;

    @Column(name = "tenmanhinhduocload", nullable = false, length = 100)
    private String screenName;
}
