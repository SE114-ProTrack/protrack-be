package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nhancongviec")
public class Label {
    @Id
    @GeneratedValue
    @Column(name = "id_nhan", columnDefinition = "BINARY(16)")
    private UUID labelId;

    @ManyToOne
    @JoinColumn(name = "id_duan", nullable = false)
    private Project project;

    @Column(name = "tennhan", nullable = false)
    private String labelName;

    @Column(name = "mota")
    private String description;
}
