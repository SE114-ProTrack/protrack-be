package com.protrack.protrack_be.service;

import java.util.UUID;

public interface ProjectMemberService {
    boolean isProjectOwner(UUID projectId, UUID userId);
    boolean isMember(UUID projectId, UUID userId);
}
