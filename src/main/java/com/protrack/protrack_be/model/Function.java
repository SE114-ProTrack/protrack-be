package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CHUCNANG")
public class Function {
    @Id
    @GeneratedValue
    @Column(name = "ID_ChucNang", columnDefinition = "BINARY(16)")
    private UUID functionId;

    @Column(name = "FunctionCode", nullable = false, unique = true, length = 100)
    private String functionCode;

    @Column(name = "TenChucNang", nullable = false, length = 100)
    private String functionName;

    @Column(name = "TenManHinhDuocLoad", nullable = false, length = 100)
    private String screenName;
}
