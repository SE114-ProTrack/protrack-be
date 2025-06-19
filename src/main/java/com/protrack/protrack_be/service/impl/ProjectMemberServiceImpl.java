package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.ProjectMemberRequest;
import com.protrack.protrack_be.dto.response.ProjectMemberResponse;
import com.protrack.protrack_be.mapper.InvitationMapper;
import com.protrack.protrack_be.mapper.ProjectMemberMapper;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.model.ProjectMember;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.model.id.ProjectMemberId;
import com.protrack.protrack_be.repository.ProjectMemberRepository;
import com.protrack.protrack_be.service.ProjectMemberService;
import com.protrack.protrack_be.service.ProjectService;
import com.protrack.protrack_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.ProjectMemberMapper.toResponse;

public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Autowired
    ProjectMemberRepository repo;

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Override
    public List<ProjectMemberResponse> getAll(){
        return repo.findAll().stream()
                .map(ProjectMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectMemberResponse> getById(ProjectMemberId id){
        return repo.findById(id)
                .map(ProjectMemberMapper::toResponse);
    }

    @Override
    public ProjectMemberResponse create(ProjectMemberRequest request){
        ProjectMember projectMember = new ProjectMember();
        Project project = projectService.getEntityById(request.getProjectId())
                        .orElseThrow(() -> new RuntimeException("Can not find project"));
        User user = userService.getCurrentUser();

        ProjectMemberId id = new ProjectMemberId(project.getProjectId(), user.getUserId());

        projectMember.setId(id);
        projectMember.setIsProjectOwner(request.getIsProjectOwner());

        ProjectMember saved = repo.save(projectMember);

        return toResponse(saved);
    }

    @Override
    public ProjectMemberResponse update(ProjectMemberId id, ProjectMemberRequest request){
        ProjectMember projectMember = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not find project member"));

        if(request.getIsProjectOwner() != null) projectMember.setIsProjectOwner(request.getIsProjectOwner());

        ProjectMember saved = repo.save(projectMember);

        return toResponse(saved);
    }

    @Override
    public void delete(ProjectMemberId id){ repo.deleteById(id); }
}
