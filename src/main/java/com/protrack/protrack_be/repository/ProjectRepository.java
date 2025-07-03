package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Project;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query(value = """
            SELECT * FROM duan d
            WHERE d.id_duan IN (
                SELECT id_duan FROM thanhvienduan WHERE id_nguoidung = :userId
            )
            AND d.da_xoa = false
            """, nativeQuery = true)
    Page<Project> findProjectsByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT COUNT(t.taskId) FROM Task t WHERE t.project.projectId = :projectId")
    int getNumberOfTasks(@Param("projectId") UUID projectId);

    @Query("SELECT COUNT(t.taskId) FROM Task t WHERE t.project.projectId = :projectId AND t.status = 'DONE'")
    int getNumberOfCompletedTasks(@Param("projectId") UUID projectId);

    @Query(value = """
            SELECT * FROM duan d
            WHERE d.id_duan IN (
                SELECT id_duan FROM thanhvienduan WHERE id_nguoidung = :userId
            )
            AND d.da_xoa = false
            ORDER BY d.thoi_gian_tao DESC
            LIMIT 3
            """, nativeQuery = true)
    List<Project> findTop3ProjectsByUserId(@Param("userId") UUID userId);

    List<Project> findByProjectNameContainingIgnoreCase(String keyword);

    Page<Project> findAll(Pageable pageable);
}
