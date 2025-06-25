package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.CommentRequest;
import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.request.ProjectRequest;
import com.protrack.protrack_be.dto.response.CommentResponse;
import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.exception.NotFoundException;
import com.protrack.protrack_be.mapper.CommentMapper;
import com.protrack.protrack_be.mapper.ProjectMapper;
import com.protrack.protrack_be.model.*;
import com.protrack.protrack_be.repository.*;
import com.protrack.protrack_be.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<CommentResponse> getCommentsByTask(UUID taskId) {
        Task task = taskService.getTask(taskId);
        if (!taskService.isVisibleToUser(task, userService.getCurrentUser().getUserId())) throw new AccessDeniedException("Bạn không được xem task này");

        return repo.findByTask_TaskIdOrderByTimestampAsc(taskId)
                .stream()
                .map(CommentMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public CommentResponse createComment(CommentRequest request) {
        Task task = taskService.getTask(request.getTaskId());
        User user = userService.getCurrentUser();

        if (!taskService.isVisibleToUser(task, user.getUserId())) {
            throw new AccessDeniedException("Bạn không có quyền bình luận task này");
        }

        Comment comment = new Comment();
        comment.setCommentId(UUID.randomUUID());
        comment.setTask(task);
        comment.setUser(user);
        comment.setContent(request.getContent());
        comment.setTimestamp(LocalDateTime.now());

        Comment saved = repo.save(comment);
        List<UUID> receivers = getCommentNotifiedUsers(task, user);

        for (UUID receiverId : receivers) {
            notificationService.create(new NotificationRequest(
                    receiverId,
                    "COMMENT",
                    user.getName() + " đã bình luận vào công việc \"" + task.getTaskName() + "\""
            ));
        }
        return CommentMapper.toResponse(saved);
    }

    public Optional<CommentResponse> getById(UUID id) {
        return repo.findById(id)
                .map(CommentMapper::toResponse);
    }

    @Override
    public CommentResponse update(UUID id, CommentRequest request){
        Comment comment = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not find project"));

        if(request.getContent() != null) comment.setContent(request.getContent());

        Comment saved = repo.save(comment);

        return CommentMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id){ repo.deleteById(id); }

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
