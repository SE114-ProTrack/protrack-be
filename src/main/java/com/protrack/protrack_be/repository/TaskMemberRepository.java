package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.TaskMember;
import com.protrack.protrack_be.model.id.TaskMemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskMemberRepository extends JpaRepository<TaskMember, TaskMemberId> {
    boolean existsByTask_TaskIdAndUser_UserId(UUID taskId, UUID userId);
    void deleteByTask_TaskId(UUID taskId);
    List<TaskMember> findByTask_TaskId(UUID taskId);
    Page<TaskMember> findByTask_TaskId(UUID taskId, Pageable pageable);

    @Query("SELECT COUNT(tm) FROM TaskMember tm WHERE tm.user.userId = :userId")
    int countTasksByUser(@Param("userId") UUID userId);

}
