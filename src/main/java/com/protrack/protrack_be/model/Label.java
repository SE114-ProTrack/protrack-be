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
@Table(name = "NHANCONGVIEC")
public class Label {
    @Id
    @GeneratedValue
    @Column(name = "ID_Nhan", columnDefinition = "BINARY(16)")
    private UUID labelId;

    @ManyToOne
    @JoinColumn(name = "ID_DuAn", nullable = false)
    private Project project;

    @Column(name = "TenNhan", nullable = false)
    private String labelName;

    @Column(name = "MoTa")
    private String description;
}
