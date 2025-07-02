package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.dto.response.LabelResponse;
import com.protrack.protrack_be.model.Label;
import com.protrack.protrack_be.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LabelRepository extends JpaRepository<Label, UUID> {
    Optional<Label> findByProject_ProjectId(UUID projectId);
    boolean existsByProjectAndLabelName(Project project, String labelName);
}
