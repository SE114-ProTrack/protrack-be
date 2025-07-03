package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByProject_ProjectId(UUID projectId);

    @Query("SELECT DISTINCT t FROM Task t JOIN TaskMember tm ON t.taskId = tm.task.taskId WHERE t.project.projectId = :projectId AND tm.user.userId = :userId")
    Page<Task> findAllByProjectIdAndUserId(@Param("projectId") UUID projectId, @Param("userId") UUID userId, Pageable pageable);

    boolean existsByParentTask_TaskId(UUID taskId);

    @Query("SELECT t FROM Task t JOIN TaskMember tm ON t.taskId = tm.task.taskId WHERE tm.user.userId = :userId")
    List<Task> findTasksByUserId(@Param("userId") UUID userId);

    @Query(value = """
            SELECT * FROM congviec c
            JOIN nguoithuchiencv nth ON nth.id_congviec = c.id_congviec
            WHERE nth.id_nguoidung = :userId AND c.da_xoa = false
            ORDER BY c.thoi_gian_tao DESC
            LIMIT 3
            """, nativeQuery = true)
    List<Task> findTop3TasksByUserId(@Param("userId") UUID userId);

    List<Task> findByTaskNameContainingIgnoreCase(String keyword);

    List<Task> findByParentTask_TaskId(UUID parentTaskId);
}

