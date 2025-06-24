package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.TaskDetailRequest;
import com.protrack.protrack_be.dto.response.TaskDetailResponse;
import com.protrack.protrack_be.mapper.TaskDetailMapper;
import com.protrack.protrack_be.model.Task;
import com.protrack.protrack_be.model.TaskDetail;
import com.protrack.protrack_be.model.id.TaskDetailId;
import com.protrack.protrack_be.repository.TaskDetailRepository;
import com.protrack.protrack_be.service.TaskDetailService;
import com.protrack.protrack_be.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.TaskDetailMapper.toResponse;

public class TaskDetailServiceImpl implements TaskDetailService {
    @Autowired
    TaskDetailRepository repo;

    @Autowired
    TaskService taskService;

    @Override
    public List<TaskDetailResponse> getAll(){
        return repo.findAll()
                .stream()
                .map(TaskDetailMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TaskDetailResponse> getById(TaskDetailId id){
        return repo.findById(id)
                .map(TaskDetailMapper::toResponse);
    }

    @Override
    public TaskDetailResponse create(TaskDetailRequest request){
        Task parent = taskService.getTask(request.getParentTaskId());
        Task child = taskService.getTask(request.getChildTaskId());
        TaskDetailId id = new TaskDetailId(request.getParentTaskId(), request.getChildTaskId());
        TaskDetail taskDetail = new TaskDetail();

        taskDetail.setId(id);
        taskDetail.setParentTaskId(parent);
        taskDetail.setChildTaskId(child);

        TaskDetail saved = repo.save(taskDetail);

        return toResponse(saved);
    }

    @Override
    public void delete(TaskDetailId id){ repo.deleteById(id); }
}
