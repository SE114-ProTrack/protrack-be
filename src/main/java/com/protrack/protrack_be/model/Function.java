package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chucnang")
public class Function {
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
