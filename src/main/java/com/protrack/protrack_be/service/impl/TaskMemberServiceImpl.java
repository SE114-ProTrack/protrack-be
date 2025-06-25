package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.TaskMemberRequest;
import com.protrack.protrack_be.dto.response.TaskMemberResponse;
import com.protrack.protrack_be.mapper.TaskMemberMapper;
import com.protrack.protrack_be.model.Task;
import com.protrack.protrack_be.model.TaskMember;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.model.id.TaskMemberId;
import com.protrack.protrack_be.repository.TaskMemberRepository;
import com.protrack.protrack_be.service.TaskMemberService;
import com.protrack.protrack_be.service.TaskService;
import com.protrack.protrack_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<TaskMemberResponse> getAll(){
        return repo.findAll()
                .stream()
                .map(TaskMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TaskMemberResponse> getById(TaskMemberId id){
        return repo.findById(id)
                .map(TaskMemberMapper::toResponse);
    }

    @Override
    public TaskMemberResponse create(TaskMemberRequest request){
        Task task = taskService.getTask(request.getTaskId());
        User user = userService.getUserById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Can not find user"));
        TaskMemberId id = new TaskMemberId(request.getTaskId(), request.getUserId());
        TaskMember taskMember = new TaskMember();

        taskMember.setId(id);
        taskMember.setTask(task);
        taskMember.setUser(user);

        TaskMember saved = repo.save(taskMember);

        return toResponse(saved);
    }

    @Override
    public void delete(TaskMemberId id){ repo.deleteById(id); }

    @Override
    public List<TaskMember> getMembersByTask(UUID taskId) {
        return repo.findByTask_TaskId(taskId);
    }

    @Override
    public List<UUID> getAssigneeIds(UUID taskId) {
        return getMembersByTask(taskId).stream()
                .map(tm -> tm.getUser().getUserId())
                .toList();
    }
}
