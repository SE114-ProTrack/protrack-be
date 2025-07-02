package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.ActivityHistoryRequest;
import com.protrack.protrack_be.dto.request.CommentRequest;
import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.request.ProjectRequest;
import com.protrack.protrack_be.dto.response.CommentResponse;
import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.enums.ProjectFunctionCode;
import com.protrack.protrack_be.exception.NotFoundException;
import com.protrack.protrack_be.mapper.CommentMapper;
import com.protrack.protrack_be.mapper.ProjectMapper;
import com.protrack.protrack_be.model.*;
import com.protrack.protrack_be.repository.*;
import com.protrack.protrack_be.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.protrack.protrack_be.mapper.ProjectMapper.toResponse;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    @Autowired
    private final CommentRepository repo;

    @Autowired
    private final UserService userService;

    @Autowired
    private final TaskService taskService;

    @Autowired
    private final TaskMemberService taskMemberService;

    @Autowired
    private final NotificationService notificationService;

    @Autowired
    private final ActivityHistoryService activityHistoryService;

    @Autowired
    private final ProjectPermissionService projectPermissionService;

    @Override
    @EnableSoftDeleteFilter
    public Page<CommentResponse> getCommentsByTask(UUID taskId, Pageable pageable) {
        Task task = taskService.getTask(taskId);
        if (!taskService.isVisibleToUser(task, userService.getCurrentUser().getUserId())) throw new AccessDeniedException("You are not permitted to view this task");

        return repo.findByTask_TaskIdOrderByCreatedAtAsc(taskId, pageable)
                .map(CommentMapper::toResponse);
    }

    @Transactional
    @Override
    public CommentResponse createComment(CommentRequest request) {
        Task task = taskService.getTask(request.getTaskId());
        User user = userService.getCurrentUser();

        if (!taskService.isVisibleToUser(task, user.getUserId())) {
            throw new AccessDeniedException("You are not permitted to comment on this task");
        }

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setContent(request.getContent());

        Comment saved = repo.save(comment);
        List<UUID> receivers = getCommentNotifiedUsers(task, user);

        for (UUID receiverId : receivers) {
            notificationService.create(new NotificationRequest(
                    receiverId,
                    "COMMENT",
                    user.getName() + " has commented on \"" + task.getTaskName() + "\"",
                    ""
            ));
        }

        activityHistoryService.create(
                new ActivityHistoryRequest(
                        comment.getTask().getTaskId(),
                        "COMMENT_CREATED",
                        user.getName() + " has commented: \"" + request.getContent() + "\""
                )
        );
        return CommentMapper.toResponse(saved);
    }

    public Optional<CommentResponse> getById(UUID id) {
        return repo.findById(id)
                .map(CommentMapper::toResponse);
    }

    @Override
    public CommentResponse update(UUID id, CommentRequest request){
        Comment comment = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Can not find comment"));
        User user = userService.getCurrentUser();
        Task task = comment.getTask();

        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("You are not the comment's owner");
        }

        if(request.getTaskId() != null) {
            comment.setTask(task);
        }

        String oldContent = comment.getContent();
        if(request.getContent() != null) comment.setContent(request.getContent());

        activityHistoryService.create(
                new ActivityHistoryRequest(
                        comment.getTask().getTaskId(),
                        "COMMENT_UPDATED",
                        user.getName() + " has updated their comment from: \"" + oldContent + "\" to \"" + request.getContent() + "\""
                )
        );

        Comment saved = repo.save(comment);

        return CommentMapper.toResponse(saved);
    }

    @Override
    @EnableSoftDeleteFilter
    public void delete(UUID id){
        Comment comment = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Can not find comment"));
        User user = userService.getCurrentUser();
        Task task = taskService.getTask(comment.getTask().getTaskId());

        if (!(comment.getUser().getUserId().equals(user.getUserId()) || !projectPermissionService.hasPermission(task.getProject().getProjectId(), user.getUserId(), ProjectFunctionCode.EDIT_TASK))) {
            throw new AccessDeniedException("You must be the comment's owner or permitted to delete comment in this project");
        }

        activityHistoryService.create(
                new ActivityHistoryRequest(
                        comment.getTask().getTaskId(),
                        "COMMENT_DELETED",
                        user.getName() + " has removed a comment"
                )
        );
        repo.deleteById(id);
    }

    private List<UUID> getCommentNotifiedUsers(Task task, User commenter) {
        Set<UUID> notifiedUserIds = new HashSet<>();

        if (!task.getApprover().getUserId().equals(commenter.getUserId()))
            notifiedUserIds.add(task.getApprover().getUserId());

        List<TaskMember> assignees = taskMemberService.getMembersByTask(task.getTaskId());
        for (TaskMember member : assignees) {
            UUID id = member.getUser().getUserId();
            if (!id.equals(commenter.getUserId())) {
                notifiedUserIds.add(id);
            }
        }

        return new ArrayList<>(notifiedUserIds);
    }

}
