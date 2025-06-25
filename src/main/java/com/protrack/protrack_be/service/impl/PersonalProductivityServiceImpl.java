package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.PersonalProductivityRequest;
import com.protrack.protrack_be.dto.response.PersonalProductivityResponse;
import com.protrack.protrack_be.mapper.PersonalProductivityMapper;
import com.protrack.protrack_be.model.PersonalProductivity;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.model.id.PersonalProductivityId;
import com.protrack.protrack_be.repository.PersonalProductivityRepository;
import com.protrack.protrack_be.service.PersonalProductivityService;
import com.protrack.protrack_be.service.ProjectService;
import com.protrack.protrack_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.PersonalProductivityMapper.toResponse;

@Service
public class PersonalProductivityServiceImpl implements PersonalProductivityService {

    @Autowired
    PersonalProductivityRepository repo;

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Override
    public List<PersonalProductivityResponse> getAll(){
        return repo.findAll().stream()
                .map(PersonalProductivityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PersonalProductivityResponse> getById(PersonalProductivityId id){
        return repo.findById(id)
                .map(PersonalProductivityMapper::toResponse);
    }

    @Override
    public PersonalProductivityResponse save(PersonalProductivityRequest request){
        User user = userService.getUserById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Can not find user"));
        Project project = projectService.getEntityById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Can not find project"));
        PersonalProductivityId id = new PersonalProductivityId(request.getUserId(), request.getProjectId());
        PersonalProductivity productivity = repo.findById(id)
                .orElse(new PersonalProductivity());

        if(productivity.getId() == null){
            productivity.setId(id);
        }

        productivity.setUser(user);
        productivity.setProject(project);
        productivity.setCompletedTasks(request.getCompletedTasks());
        productivity.setLastUpdated(LocalDateTime.now());

        PersonalProductivity saved = repo.save(productivity);

        return toResponse(saved);
    }
}
