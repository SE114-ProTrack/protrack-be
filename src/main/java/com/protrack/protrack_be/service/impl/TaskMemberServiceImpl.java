package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.ActivityHistoryRequest;
import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.request.TaskMemberRequest;
import com.protrack.protrack_be.dto.response.TaskMemberResponse;
import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.exception.AccessDeniedException;
import com.protrack.protrack_be.exception.BadRequestException;
import com.protrack.protrack_be.exception.NotFoundException;
import com.protrack.protrack_be.mapper.TaskMemberMapper;
import com.protrack.protrack_be.model.ActivityHistory;
import com.protrack.protrack_be.model.Task;
import com.protrack.protrack_be.model.TaskMember;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.model.id.TaskMemberId;
import com.protrack.protrack_be.repository.TaskMemberRepository;
import com.protrack.protrack_be.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.TaskMemberMapper.toResponse;

@Service
public class TaskMemberServiceImpl implements TaskMemberService {

    @Autowired
    TaskMemberRepository repo;

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Autowired
    ActivityHistoryService activityHistoryService;

    @Autowired
    ProjectPermissionService projectPermissionService;

    @Autowired
    ProjectMemberService projectMemberService;

    @Autowired
    NotificationService notificationService;

    @Override
    @EnableSoftDeleteFilter
    public List<TaskMemberResponse> getAll(){
        return repo.findAll()
                .stream()
                .map(TaskMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<TaskMemberResponse> getById(TaskMemberId id){
        return repo.findById(id)
                .map(TaskMemberMapper::toResponse);
    }

    @Transactional
    @Override
    public TaskMemberResponse create(TaskMemberRequest request){
        Task task = taskService.getTask(request.getTaskId());
        User user = userService.getUserById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Can not find user"));

        if (!projectPermissionService.hasPermission(user.getUserId(), task.getProject().getProjectId(), ProjectFunctionCode.EDIT_TASK)) {
            throw new AccessDeniedException("You are not permitted to manage task members.");
        }

        if (!projectMemberService.isMember(task.getProject().getProjectId(), request.getUserId())) {
            throw new BadRequestException("User is not a member of this project.");
        }

        if (repo.existsByTask_TaskIdAndUser_UserId(request.getTaskId(), request.getUserId())) {
            throw new BadRequestException("User is already assigned to this task.");
        }

        TaskMemberId id = new TaskMemberId(request.getTaskId(), request.getUserId());
        TaskMember taskMember = new TaskMember();

        taskMember.setId(id);
        taskMember.setTask(task);
        taskMember.setUser(user);

        TaskMember saved = repo.save(taskMember);

        activityHistoryService.create(
                new ActivityHistoryRequest(
                        task.getTaskId(),
                        "TASK_NEW_ASSIGNEE",
                        user.getName() + " has assigned +\"" + user.getName() + "\" to the task: " + task.getTaskName()
                )
        );

        notificationService.create(new NotificationRequest(
                userService.getCurrentUser().getUserId(),
                user.getUserId(),
                "TASK_NEW_ASSIGNEE",
                "You have been assigned to the task: " + task.getTaskName(),
                ""
        ));

        return toResponse(saved);
    }

    @Transactional
    @Override
    public void delete(TaskMemberId id){
        TaskMember taskMember = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Can not find task member"));

        User user = userService.getCurrentUser();
        if (!projectPermissionService.hasPermission(user.getUserId(), taskMember.getTask().getProject().getProjectId(), ProjectFunctionCode.EDIT_TASK)) {
            throw new AccessDeniedException("You are not permitted to manage task members.");
        }

        List<TaskMember> members = repo.findByTask_TaskId(taskMember.getTask().getTaskId());
        if (members.size() <= 1) {
            throw new BadRequestException("Task must have at least one assignee.");
        }

        activityHistoryService.create(
                new ActivityHistoryRequest(
                        taskMember.getTask().getTaskId(),
                        "TASK_NEW_ASSIGNEE",
                        user.getName() + " has removed +\"" + user.getName() + "\" from the task: " + taskMember.getTask().getTaskName()
                )
        );

        notificationService.create(new NotificationRequest(
                userService.getCurrentUser().getUserId(),
                taskMember.getUser().getUserId(),
                "TASK_REMOVED",
                "You have been removed from the task: " + taskMember.getTask().getTaskName(),
                ""
        ));

        repo.deleteById(id);
    }

    @Override
    @EnableSoftDeleteFilter
    public List<TaskMember> getMembersByTask(UUID taskId) {
        return repo.findByTask_TaskId(taskId);
    }

    @Override
    public Page<TaskMemberResponse> getMembersByTask(UUID taskId, Pageable pageable) {
        return repo.findByTask_TaskId(taskId, pageable)
                .map(TaskMemberMapper::toResponse);
    }

    @Override
    @EnableSoftDeleteFilter
    public List<UUID> getAssigneeIds(UUID taskId) {
        return getMembersByTask(taskId).stream()
                .map(tm -> tm.getUser().getUserId())
                .toList();
    }
}
