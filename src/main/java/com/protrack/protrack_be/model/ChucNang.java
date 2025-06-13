package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CHUCNANG")
public class ChucNang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ChucNang")
    private Long fuctionId;

    @Column(name = "TenChucNang", nullable = false, length = 100)
    private String fuctionName;

    @Column(name = "TenManHinhDuocLoad", nullable = false, length = 100)
    private String screenName;
}
