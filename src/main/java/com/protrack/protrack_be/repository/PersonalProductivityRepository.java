package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.PersonalProductivity;
import com.protrack.protrack_be.model.id.PersonalProductivityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalProductivityRepository extends JpaRepository<PersonalProductivity, PersonalProductivityId> {}

