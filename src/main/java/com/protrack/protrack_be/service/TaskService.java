package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.TaskRequest;
import com.protrack.protrack_be.dto.request.TaskStatusRequest;
import com.protrack.protrack_be.dto.response.TaskResponse;
import com.protrack.protrack_be.model.Task;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    List<TaskResponse> getTasks(UUID projectId, UUID userId);
    TaskResponse createTask(TaskRequest request, UUID userId);
    Optional<TaskResponse> getTaskById(UUID taskId, UUID userId);
    TaskResponse updateTask(UUID taskId, TaskRequest request, UUID userId);
    void deleteTask(UUID taskId, UUID userId);
    TaskResponse updateTaskStatus(UUID taskId, TaskStatusRequest request, UUID userId);
    Task getTask(UUID taskId);
    boolean isVisibleToUser(Task task, UUID userId);
    List<UUID> getAssigneeIds(Task task);
}
