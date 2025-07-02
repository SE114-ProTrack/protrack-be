package com.protrack.protrack_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@SQLDelete(sql = "UPDATE thongbao SET da_xoa = true WHERE id_thongbao = ?")
@Filter(name = "deletedFilter", condition = "da_xoa = :isDeleted")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "thongbao")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "id_thongbao", columnDefinition = "BINARY(16)")
    private UUID notificationId;

    @ManyToOne
    @JoinColumn(name = "id_nguoigui", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "id_nguoinhan", nullable = false)
    private User receiver;

    @Column(name = "loaithongbao", nullable = false)
    private String type;

    @Column(name = "noidung", nullable = false)
    private String content;

    @Column(name = "dadoc")
    private Boolean isRead = false;

    @Column(name = "duongdan")
    private String actionUrl;
}
