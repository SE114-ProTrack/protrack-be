package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@SQLDelete(sql = "UPDATE tinnhan SET da_xoa = true WHERE id_tinnhan = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tinnhan")
public class Message extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "id_tinnhan", columnDefinition = "BINARY(16)")
    private UUID messageId;

    @ManyToOne
    @JoinColumn(name = "id_nguoigui", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "id_nguoinhan", nullable = false)
    private User receiver;

    @Column(name = "noidung", nullable = false)
    private String content;

    @Column(name = "dadoc")
    private boolean read = false;

    @Column(name = "thoigiandoc")
    private LocalDateTime readAt;
}
