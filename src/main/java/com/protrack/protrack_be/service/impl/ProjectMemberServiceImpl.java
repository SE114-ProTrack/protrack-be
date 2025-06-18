package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.ProjectMemberRequest;
import com.protrack.protrack_be.dto.response.ProjectMemberResponse;
import com.protrack.protrack_be.mapper.InvitationMapper;
import com.protrack.protrack_be.mapper.ProjectMemberMapper;
import com.protrack.protrack_be.model.ProjectMember;
import com.protrack.protrack_be.model.id.ProjectMemberId;
import com.protrack.protrack_be.repository.ProjectMemberRepository;
import com.protrack.protrack_be.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectMemberServiceImpl implements ProjectMemberService {
    @Autowired
    ProjectMemberRepository repo;

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
        return new ProjectMemberResponse();
    }

    @Override
    public ProjectMemberResponse update(ProjectMemberId id, ProjectMemberRequest request){
        return new ProjectMemberResponse();
    }

    @Override
    public void delete(ProjectMemberId id){ repo.deleteById(id); }
}
