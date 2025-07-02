package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.ActivityHistoryRequest;
import com.protrack.protrack_be.dto.response.ActivityHistoryResponse;
import com.protrack.protrack_be.mapper.ActivityHistoryMapper;
import com.protrack.protrack_be.model.ActivityHistory;
import com.protrack.protrack_be.model.Task;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.repository.ActivityHistoryRepository;
import com.protrack.protrack_be.service.ActivityHistoryService;
import com.protrack.protrack_be.service.TaskService;
import com.protrack.protrack_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.ActivityHistoryMapper.toResponse;

@Service
public class ActivityHistoryServiceImpl implements ActivityHistoryService {

    @Autowired
    ActivityHistoryRepository repo;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Override
    public List<ActivityHistoryResponse> getAll(){
        return repo.findAll().stream()
                .map(ActivityHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ActivityHistoryResponse> getById(UUID id){
        return repo.findById(id)
                .map(ActivityHistoryMapper::toResponse);
    }

    @Override
    public List<ActivityHistoryResponse> getActivityHistoryByTask(UUID taskId) {
        Task task = taskService.getTask(taskId);
        if (!taskService.isVisibleToUser(task, userService.getCurrentUser().getUserId())) throw new AccessDeniedException("Bạn không được xem task này");

        return repo.findByTask_TaskIdOrderByCreatedAtAsc(taskId)
                .stream()
                .map(ActivityHistoryMapper::toResponse)
                .toList();
    }

    @Override
    public ActivityHistoryResponse create(ActivityHistoryRequest request){
        ActivityHistory activityHistory = new ActivityHistory();
        User user = userService.getUserById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        Task task = taskService.getTask(request.getTaskId());

        activityHistory.setUser(user);
        activityHistory.setTask(task);
        activityHistory.setActionType(request.getActionType());
        activityHistory.setDescription(request.getDescription());

        ActivityHistory saved = repo.save(activityHistory);

        return toResponse(saved);
    }


}
