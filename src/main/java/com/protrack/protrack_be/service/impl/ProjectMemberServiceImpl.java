package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.repository.ProjectMemberRepository;
import com.protrack.protrack_be.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository memberRepository;

    @Override
    public boolean isProjectOwner(UUID projectId, UUID userId) {
        return memberRepository.existsByProject_ProjectIdAndUser_UserIdAndIsProjectOwnerTrue(projectId, userId);
    }

    @Override
    public boolean isMember(UUID projectId, UUID userId) {
        return memberRepository.existsByProject_ProjectIdAndUser_UserId(projectId, userId);
    }
}
