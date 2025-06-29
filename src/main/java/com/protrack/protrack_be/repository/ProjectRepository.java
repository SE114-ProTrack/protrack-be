package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query(value = """
            SELECT * FROM duan d
            JOIN thanhvienduan tv ON tv.id_duan = d.id_duan
            WHERE tv.id_nguoidung = :userId
            """, nativeQuery = true)
    List<Project> findProjectsByUserId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(t.taskId) FROM Task t WHERE t.project.projectId = :projectId")
    int getNumberOfTasks(@Param("projectId") UUID projectId);

    @Query("SELECT COUNT(t.taskId) FROM Task t WHERE t.project.projectId = :projectId AND t.status = 'DONE'")
    int getNumberOfCompletedTasks(@Param("projectId") UUID projectId);

    @Query(value = """
            SELECT * FROM duan d
            JOIN thanhvienduan tv ON tv.id_duan = d.id_duan
            WHERE tv.id_nguoidung = :userId
            ORDER BY d.createTime DESC
            LIMIT 3
            """, nativeQuery = true)
    List<Project> findTop3ProjectsByUserId(@Param("userId") UUID userId);
}
