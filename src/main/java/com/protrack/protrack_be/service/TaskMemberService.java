package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.TaskMemberRequest;
import com.protrack.protrack_be.dto.response.TaskMemberResponse;
import com.protrack.protrack_be.model.id.TaskMemberId;

import java.util.List;
import java.util.Optional;

public interface TaskMemberService {
    List<TaskMemberResponse> getAll();
    Optional<TaskMemberResponse> getById(TaskMemberId id);
    TaskMemberResponse create(TaskMemberRequest request);
    void delete(TaskMemberId id);
}
