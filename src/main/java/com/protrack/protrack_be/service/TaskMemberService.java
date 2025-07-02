package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.TaskMemberRequest;
import com.protrack.protrack_be.dto.response.TaskMemberResponse;
import com.protrack.protrack_be.model.TaskMember;
import com.protrack.protrack_be.model.id.TaskMemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskMemberService {
    List<TaskMemberResponse> getAll();
    Optional<TaskMemberResponse> getById(TaskMemberId id);
    TaskMemberResponse create(TaskMemberRequest request);
    void delete(TaskMemberId id);
    List<TaskMember> getMembersByTask(UUID taskId);
    Page<TaskMemberResponse> getMembersByTask(UUID taskId, Pageable pageable);
    List<UUID> getAssigneeIds(UUID taskId);
}
