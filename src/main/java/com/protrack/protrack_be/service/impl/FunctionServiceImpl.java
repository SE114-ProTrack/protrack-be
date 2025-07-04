package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.FunctionRequest;
import com.protrack.protrack_be.dto.response.FunctionResponse;
import com.protrack.protrack_be.exception.BadRequestException;
import com.protrack.protrack_be.mapper.FunctionMapper;
import com.protrack.protrack_be.model.Function;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.model.ProjectMember;
import com.protrack.protrack_be.model.ProjectPermission;
import com.protrack.protrack_be.model.id.ProjectPermissionId;
import com.protrack.protrack_be.repository.FunctionRepository;
import com.protrack.protrack_be.repository.ProjectMemberRepository;
import com.protrack.protrack_be.repository.ProjectPermissionRepository;
import com.protrack.protrack_be.repository.ProjectRepository;
import com.protrack.protrack_be.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.FunctionMapper.toEntity;
import static com.protrack.protrack_be.mapper.FunctionMapper.toResponse;

@Service
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    FunctionRepository repo;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectPermissionRepository projectPermissionRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Override
    public List<Function> getDefaults() {
        return repo.findByFunctionCodeIn(List.of("VIEW_TASK"));
    }

    @Override
    @EnableSoftDeleteFilter
    public List<Function> getAllEntities() {
        return repo.findAll();
    }

    @Override
    @EnableSoftDeleteFilter
    public List<FunctionResponse> getAll(){
        return repo.findAll().stream()
                .map(FunctionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<FunctionResponse> getById(UUID id){
        return repo.findById(id)
                .map(FunctionMapper::toResponse);
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<Function> getEntityById(UUID id){
        return repo.findById(id);
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<Function> getEntityByFunctionCode(String functionCode){
        return repo.findByFunctionCode(functionCode);
    }

    @Override
    @EnableSoftDeleteFilter
    public FunctionResponse create(FunctionRequest request){
        if (repo.findByFunctionCode(request.getFunctionCode()).isPresent()) {
            throw new BadRequestException("Function code already exists!");
        }
        Function function = toEntity(request);
        Function saved = repo.save(function);
        List<Project> allProjects = projectRepository.findAll();
        for (Project project : allProjects) {
            List<ProjectMember> members = projectMemberRepository.findAllByProject_ProjectId(project.getProjectId());
            for (ProjectMember member : members) {
                ProjectPermissionId id = new ProjectPermissionId(project.getProjectId(), member.getUser().getUserId(), saved.getFunctionId());
                if (!projectPermissionRepository.existsById(id)) {
                    ProjectPermission permission = new ProjectPermission(id, project, member.getUser(), saved, false);
                    projectPermissionRepository.save(permission);
                }
            }
        }

        return toResponse(saved);
    }

    @Override
    @EnableSoftDeleteFilter
    public FunctionResponse update(UUID id, FunctionRequest request){
        Function function = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Function not found"));

        if(request.getFunctionName() != null) {
            if (repo.findByFunctionCode(request.getFunctionCode()).isPresent()) {
                throw new BadRequestException("Function code already exists!");
            }
            function.setFunctionName(request.getFunctionName());
        }
        if(request.getScreenName() != null) function.setScreenName(request.getScreenName());

        Function saved = repo.save(function);
        return toResponse(saved);
    }

    @Override
    public void delete(UUID id){ repo.deleteById(id); }

    @Override
    public boolean existsById(UUID functionId) {
        return repo.existsById(functionId);
    }

}
