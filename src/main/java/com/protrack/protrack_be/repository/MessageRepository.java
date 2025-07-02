package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.dto.response.MessagePreviewResponse;
import com.protrack.protrack_be.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.userId = :user1 AND m.receiver.userId = :user2) OR " +
            "(m.sender.userId = :user2 AND m.receiver.userId = :user1)" +
            "ORDER BY m.sentAt DESC")
    Page<Message> findMessagesBetweenUsers(@Param("user1") UUID user1,
                                           @Param("user2") UUID user2,
                                           Pageable pageable);

    @Query("SELECT COUNT(m) > 0 FROM Message m " +
            "WHERE (m.sender.userId = :user1 AND m.receiver.userId = :user2) " +
            "   OR (m.sender.userId = :user2 AND m.receiver.userId = :user1)")
    boolean existsConversationBetween(@Param("user1") UUID user1, @Param("user2") UUID user2);

    @Query(value = """
            SELECT
                sub.user_id,
                u.hoten,
                u.hinhanh,
                sub.noidung,
                sub.thoigiangui,
                COALESCE(unread.count, 0) AS unread_count
            FROM (
                SELECT
                    CASE
                        WHEN t.id_nguoigui = :currentUserId THEN t.id_nguoinhan
                        ELSE t.id_nguoigui
                    END AS user_id,
                    t.noidung,
                    t.thoigiangui
                FROM tinnhan t
                JOIN (
                    SELECT
                        CASE
                            WHEN t1.id_nguoigui = :currentUserId THEN t1.id_nguoinhan
                            ELSE t1.id_nguoigui
                        END AS user_id,
                        MAX(t1.thoigiangui) AS max_time
                    FROM tinnhan t1
                    WHERE :currentUserId IN (t1.id_nguoigui, t1.id_nguoinhan)
                    GROUP BY user_id
                ) latest ON (
                    ((t.id_nguoigui = :currentUserId AND t.id_nguoinhan = latest.user_id)
                    OR (t.id_nguoinhan = :currentUserId AND t.id_nguoigui = latest.user_id))
                    AND t.thoigiangui = latest.max_time
                )
            ) sub
            JOIN nguoidung u ON u.id_nguoidung = sub.user_id
            LEFT JOIN (
                SELECT id_nguoigui, COUNT(*) AS count
                FROM tinnhan
                WHERE id_nguoinhan = :currentUserId AND dadoc = false
                GROUP BY id_nguoigui
            ) unread
            ON unread.id_nguoigui = sub.user_id
            ORDER BY sub.thoigiangui DESC
            """, nativeQuery = true)
    List<MessagePreviewResponse> findPreviewsByUserId(@Param("currentUserId") UUID currentUserId);

    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender.userId = :userId OR m.receiver.userId = :userId) " +
            "AND LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Message> searchByUserAndContent(@Param("userId") UUID userId,
                                         @Param("keyword") String keyword,
                                         Pageable pageable);
}

