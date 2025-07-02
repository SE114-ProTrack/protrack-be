package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.request.TaskAttachmentRequest;
import com.protrack.protrack_be.dto.request.TaskRequest;
import com.protrack.protrack_be.dto.request.TaskStatusRequest;
import com.protrack.protrack_be.dto.response.TaskResponse;
import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.enums.TaskStatus;
import com.protrack.protrack_be.exception.BadRequestException;
import com.protrack.protrack_be.exception.NotFoundException;
import com.protrack.protrack_be.mapper.TaskMapper;
import com.protrack.protrack_be.model.*;
import com.protrack.protrack_be.model.id.*;
import com.protrack.protrack_be.repository.*;
import com.protrack.protrack_be.service.*;
import jakarta.transaction.Transactional;
import com.protrack.protrack_be.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final ProjectMemberRepository memberRepository;

    @Autowired
    private final TaskMemberRepository taskMemberRepository;

    @Autowired
    private final TaskAttachmentRepository taskAttachmentRepository;

    @Autowired
    private final ActivityHistoryRepository historyRepository;

    @Autowired
    private final PersonalProductivityRepository productivityRepository;

    @Autowired
    private final LabelRepository labelRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final ProjectMemberService projectMemberService;

    @Autowired
    private final ProjectPermissionService projectPermissionService;

    @Autowired
    private final NotificationService notificationService;

    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    private final ProjectService projectService;

    @Autowired
    private final TaskAttachmentService taskAttachmentService;

    @Override
    @EnableSoftDeleteFilter
    public Page<TaskResponse> getTasks(UUID projectId, UUID userId, Pageable pageable) {
        return taskRepository.findAllByProjectIdAndUserId(projectId, userId, pageable)
                .map(TaskMapper::toResponse);
    }

    @Transactional
    @Override
    public TaskResponse createTask(TaskRequest request, UUID userId) {
        try {

        if (!projectService.hasProjectRight(request.getProjectId(), userId, ProjectFunctionCode.CREATE_TASK)) {
            throw new AccessDeniedException("You are not permitted to create task in this project");
        }

            checkMembership(request.getProjectId(), userId);

            Task task = buildBaseTask(request, request.getProjectId());

            if (request.getAttachments() != null) {
                for (TaskAttachmentRequest att : request.getAttachments()) {
                    TaskAttachment attachment = new TaskAttachment();
                    attachment.setTask(task);
                    attachment.setFileName(att.getFileName());
                    attachment.setFileUrl(att.getFileUrl());
                    attachment.setFileType(att.getFileType());
                    attachment.setUploadedAt(LocalDateTime.now());
                    taskAttachmentRepository.save(attachment);
                }
            }

            Task saved = taskRepository.save(task);

            assignUsersToTask(task, request.getAssigneeIds());

            if (Boolean.TRUE.equals(request.getIsMain())) {
                createSubTasks(task, request.getSubTasks());
            }

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Can not find user"));
        logActivity(task, userId, "CREATE_TASK", "Task \"" + task.getTaskName() + "\" has been created by " + user.getName());
        notifyUsers(request.getAssigneeIds(), "ASSIGN_TASK", "You have been assigned a task: " + task.getTaskName());

            return TaskMapper.toResponse(saved);
        } catch (Exception e) {
            entityManager.clear();
            throw e;
        }
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<TaskResponse> getTaskById(UUID taskId, UUID userId) {
        Task task = getTask(taskId);
        if (!isVisibleToUser(task, userId)) throw new AccessDeniedException("You are not permitted to view this task");
        return Optional.of(TaskMapper.toResponse(task));
    }

    @Transactional
    @Override
    @EnableSoftDeleteFilter
    public TaskResponse updateTask(UUID taskId, TaskRequest request, UUID userId) {
        Task task = getTask(taskId);
        if (!projectService.hasProjectRight(task.getProject().getProjectId(), userId, ProjectFunctionCode.EDIT_TASK)) {
            throw new AccessDeniedException("You are not permitted to edit this task");
        }

        updateTaskFields(task, request);

        if (request.getAttachments() != null) {
            taskAttachmentRepository.deleteByTask_TaskId(taskId);

            for (TaskAttachmentRequest att : request.getAttachments()) {
                TaskAttachment attachment = new TaskAttachment();
                attachment.setTask(task);
                attachment.setFileName(att.getFileName());
                attachment.setFileUrl(att.getFileUrl());
                attachment.setFileType(att.getFileType());
                attachment.setUploadedAt(LocalDateTime.now());
                taskAttachmentRepository.save(attachment);
            }
        }

        Task saved = taskRepository.save(task);

        List<UUID> oldAssignees = getAssigneeIds(task);
        taskMemberRepository.deleteByTask_TaskId(taskId);
        assignUsersToTask(task, request.getAssigneeIds());
        for (UUID _userId : oldAssignees) {
            updateProductivity(task.getProject().getProjectId(), _userId, -1);
        }
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Can not find user"));
        logActivity(task, userId, "UPDATE_TASK", "Task \"" + task.getTaskName() + "\" is updated by " + user.getName());
        return TaskMapper.toResponse(saved);
    }

    @Override
    public void deleteTask(UUID taskId, UUID userId) {
        Task task = getTask(taskId);
        if (!projectService.hasProjectRight(task.getProject().getProjectId(), userId, ProjectFunctionCode.DELETE_TASK)) {
            throw new AccessDeniedException("You are not permitted to delete this task");
        }

        for (UUID assignee : getAssigneeIds(task)) {
            updateProductivity(task.getProject().getProjectId(), assignee, -1);
        }

        if(taskRepository.existsByParentTask_TaskId(taskId))
            throw new BadRequestException("This task contains subtask(s). Can not delete");

        taskMemberRepository.deleteByTask_TaskId(taskId);
        historyRepository.deleteByTask_TaskId(taskId);

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Can not find user"));
        notifyUsers(getAssigneeIds(task), "DELETE_TASK", "Task \"" + task.getTaskName() + "\" has been deleted");
        logActivity(task, userId, "DELETE_TASK", "Task \"" + task.getTaskName() + "\" is deleted by " + user.getName());

        taskRepository.delete(task);
    }

    @Override
    @EnableSoftDeleteFilter
    public List<TaskResponse> getTasksByUser(UUID userId){
        return taskRepository.findTasksByUserId(userId)
                .stream()
                .map(TaskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @EnableSoftDeleteFilter
    public List<TaskResponse> get3ByUser(UUID userId){
        return taskRepository.findTop3TasksByUserId(userId)
                .stream()
                .map(TaskMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @EnableSoftDeleteFilter
    public List<TaskResponse> findByKeyword(String keyword) {
        return taskRepository.findByTaskNameContainingIgnoreCase(keyword)
                .stream()
                .map(TaskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getSubtasks(UUID parentTaskId) {
        List<Task> subtasks = taskRepository.findByParentTask_TaskId(parentTaskId);
        return subtasks.stream().map(TaskMapper::toResponse).collect(Collectors.toList());
    }

    public Optional<TaskResponse> getParentTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        if (task.getParentTask() == null) return Optional.empty();
        return Optional.of(TaskMapper.toResponse(task.getParentTask()));
    }

    // HELPERS
    @EnableSoftDeleteFilter
    public Task getTask(UUID taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));
    }

    @EnableSoftDeleteFilter
    private void validateUserIsProjectMember(UUID projectId, UUID userId) {
        if (!memberRepository.existsByProject_ProjectIdAndUser_UserId(projectId, userId)) {
            throw new BadRequestException("User " + userId + " is not a member of project " + projectId);
        }
    }
    @EnableSoftDeleteFilter
    public boolean isVisibleToUser(Task task, UUID userId) {
        UUID projectId = task.getProject().getProjectId();

        return taskMemberRepository.existsByTask_TaskIdAndUser_UserId(task.getTaskId(), userId)
                || projectMemberService.isProjectOwner(projectId, userId)
                || projectPermissionService.hasPermission(userId, projectId, ProjectFunctionCode.VIEW_TASK);
    }

    private void checkMembership(UUID projectId, UUID userId) {
        if (!projectMemberService.isMember(projectId, userId)) {
            throw new AccessDeniedException("You are not a member of this project");
        }
    }

    @EnableSoftDeleteFilter
    private Task buildBaseTask(TaskRequest request, UUID projectId) {
        Task task = new Task();
        User approver = userService.getUserById(request.getApproverId())
                .orElseThrow(() -> new NotFoundException("Can not find user"));
        task.setProject(projectRepository.getReferenceById(projectId));
        task.setTaskName(request.getTaskName());
        task.setDescription(request.getDescription());
        if (request.getDeadline() != null && request.getDeadline().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Deadline must be after now.");
        }
        task.setDeadline(request.getDeadline());
        task.setPriority(request.getPriority());
        if (request.getAttachments() != null) {
            for (TaskAttachmentRequest att : request.getAttachments()) {
                TaskAttachment a = new TaskAttachment();
                a.setTask(task);
                a.setFileName(att.getFileName());
                a.setFileUrl(att.getFileUrl());
                a.setFileType(att.getFileType());
                a.setUploadedAt(LocalDateTime.now());
                taskAttachmentRepository.save(a);
            }
        }
        task.setIsMain(request.getIsMain());
        task.setApprover(approver);
        if (request.getLabelId() != null)
            task.setLabel(labelRepository.findById(request.getLabelId()).orElse(null));
        if (request.getParentTaskId() != null)
            task.setParentTask(getTask(request.getParentTaskId()));
        task.setColor(request.getColor());
        task.setIcon(request.getIcon());
        try {
            TaskStatus status = TaskStatus.valueOf(request.getStatus().toUpperCase());
            task.setStatus(status);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid task status.");
        }
        return task;
    }

    private void assignUsersToTask(Task task, List<UUID> assigneeIds) {
        if (assigneeIds == null) return;

        UUID projectId = task.getProject().getProjectId();

        for (UUID assigneeId : assigneeIds) {
            validateUserIsProjectMember(projectId, assigneeId);

            taskMemberRepository.save(new TaskMember(
                    new TaskMemberId(task.getTaskId(), assigneeId),
                    task,
                    userService.getUserById(assigneeId)
                            .orElseThrow(() -> new NotFoundException("Can not find user"))
            ));

            updateProductivity(task.getProject().getProjectId(), assigneeId, +1);
        }
    }

    @EnableSoftDeleteFilter
    public List<UUID> getAssigneeIds(Task task) {
        UUID taskId = task.getTaskId();
        return taskMemberRepository.findByTask_TaskId(taskId)
                .stream()
                .map(tm -> tm.getUser().getUserId())
                .collect(Collectors.toList());
    }

    private void createSubTasks(Task parentTask, List<String> subTaskNames) {
        if (subTaskNames == null) return;
        for (String name : subTaskNames) {
            Task sub = new Task();
            sub.setProject(parentTask.getProject());
            sub.setTaskName(name);
            sub.setIsMain(false);
            sub.setDeadline(parentTask.getDeadline());
            sub.setApprover(parentTask.getApprover());
            sub.setStatus(parentTask.getStatus());
            sub.setParentTask(parentTask);
            taskRepository.save(sub);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void logActivity(Task task, UUID actorId, String type, String description) {
        User user = userService.getUserById(actorId)
                .orElseThrow(() -> new NotFoundException("Can not find user"));

        ActivityHistory history = new ActivityHistory();
        history.setUser(user);
        history.setTask(task);
        history.setActionType(type);
        history.setDescription(description);
        historyRepository.save(history);
    }


    private void notifyUsers(List<UUID> userIds, String type, String content) {
        if (userIds == null) return;
        User current = userService.getCurrentUser();
        for (UUID userId : userIds) {
            notificationService.create(new NotificationRequest(
                    current.getUserId(),
                    userId,
                    type,
                    content,
                    ""));
        }
    }

    @EnableSoftDeleteFilter
    private void updateTaskFields(Task task, TaskRequest request) {
        if (request.getDescription() != null)
            task.setDescription(request.getDescription());

        if (request.getDeadline() != null && request.getDeadline().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Deadline must be after now.");
        }

        task.setDeadline(request.getDeadline());

        if (request.getPriority() != null)
            task.setPriority(request.getPriority());

        List<TaskAttachmentRequest> newRequests = request.getAttachments();
        List<TaskAttachment> oldAttachments = taskAttachmentService.getByTaskIdRaw(task.getTaskId());
        Set<String> oldUrls = oldAttachments.stream().map(TaskAttachment::getFileUrl).collect(Collectors.toSet());
        Set<String> newUrls = newRequests.stream().map(TaskAttachmentRequest::getFileUrl).collect(Collectors.toSet());

        for (TaskAttachment oldAttach : oldAttachments) {
            if (!newUrls.contains(oldAttach.getFileUrl())) {
                taskAttachmentService.delete(oldAttach.getId());
            }
        }

        for (TaskAttachmentRequest newAttach : newRequests) {
            if (!oldUrls.contains(newAttach.getFileUrl())) {
                taskAttachmentService.createTaskAttachment(newAttach);
            }
        }


        if (request.getApproverId() != null)
            task.setApprover(userService.getUserById(request.getApproverId())
                    .orElseThrow(() -> new NotFoundException("Can not find user")));

        if (request.getLabelId() != null)
            task.setLabel(labelRepository.findById(request.getLabelId()).orElse(null));

        if (request.getParentTaskId() != null)
            task.setParentTask(getTask(request.getParentTaskId()));

        try {
            TaskStatus status = TaskStatus.valueOf(request.getStatus().toUpperCase());
            task.setStatus(status);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid task status.");
        }
    }

    public TaskResponse updateTaskStatus(UUID taskId, TaskStatusRequest rq, UUID userId) {
        Task task = getTask(taskId);

        if (!task.getApprover().getUserId().equals(userId)) {
            throw new AccessDeniedException("Only the approver has permission to update task's status");
        }

        if (rq.getStatus() == null || rq.getStatus().isBlank()) {
            throw new BadRequestException("Task status must not be empty.");
        }

        TaskStatus newStatus;
        try {
            newStatus = TaskStatus.valueOf(rq.getStatus().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid task status.");
        }

        if (task.getStatus() == newStatus) {
            return TaskMapper.toResponse(task);
        }

        if (task.getStatus() == TaskStatus.DONE && newStatus != TaskStatus.DONE) {
            for (UUID assignee : getAssigneeIds(task)) {
                updateProductivity(task.getProject().getProjectId(), assignee, -1);
            }
        }
        if (task.getStatus() != TaskStatus.DONE && newStatus == TaskStatus.DONE) {
            for (UUID assignee : getAssigneeIds(task)) {
                updateProductivity(task.getProject().getProjectId(), assignee, +1);
            }
        }

        task.setStatus(newStatus);
        taskRepository.save(task);

        logActivity(task, userId, "STATUS_CHANGE", "Task " + task.getTaskName() + " has its status changed to " + newStatus.name());
        return TaskMapper.toResponse(task);
    }

    @EnableSoftDeleteFilter
    private void updateProductivity(UUID projectId, UUID userId, int change) {
        PersonalProductivityId id = new PersonalProductivityId(userId, projectId);
        PersonalProductivity productivity = productivityRepository.findById(id)
                .orElse(new PersonalProductivity(id, userService.getUserById(userId)
                        .orElseThrow(() -> new NotFoundException("Can not find user")), projectRepository.getReferenceById(projectId), 0, LocalDateTime.now()));

        int updated = productivity.getCompletedTasks() + change;
        productivity.setCompletedTasks(Math.max(updated, 0));

        productivityRepository.save(productivity);
    }

}
