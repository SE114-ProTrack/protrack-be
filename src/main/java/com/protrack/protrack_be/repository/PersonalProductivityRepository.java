package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.PersonalProductivity;
import com.protrack.protrack_be.model.id.PersonalProductivityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PersonalProductivityRepository extends JpaRepository<PersonalProductivity, PersonalProductivityId> {
    List<PersonalProductivity> findAllByUser_UserId(UUID userId);
    List<PersonalProductivity> findAllByProject_ProjectId(UUID projectId);
}

