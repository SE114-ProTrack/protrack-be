package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Account;
import com.protrack.protrack_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByAccount(Account account);
    Optional<User> findById(UUID userId);

    @Query("SELECT u FROM User u WHERE u.account.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = """
    SELECT DISTINCT u.*
    FROM thanhvienduan pm1
    JOIN thanhvienduan pm2 ON pm1.id_duan = pm2.id_duan
    JOIN nguoidung u ON u.id_nguoidung = pm2.id_nguoidung
    WHERE pm1.id_nguoidung = :currentUserId
      AND pm2.id_nguoidung <> :currentUserId
      AND NOT EXISTS (
          SELECT 1 FROM tinnhan t
          WHERE 
              (t.id_nguoigui = :currentUserId AND t.id_nguoinhan = pm2.id_nguoidung)
              OR
              (t.id_nguoinhan = :currentUserId AND t.id_nguoigui = pm2.id_nguoidung)
      )
    """, nativeQuery = true)
    List<User> findUsersInSameProjectWithoutConversation(@Param("currentUserId") UUID currentUserId);

}
