package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.TaskMember;
import com.protrack.protrack_be.model.id.TaskMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskMemberRepository extends JpaRepository<TaskMember, TaskMemberId> {
    boolean existsByTask_TaskIdAndUser_UserId(UUID taskId, UUID userId);
    void deleteByTask_TaskId(UUID taskId);
}
