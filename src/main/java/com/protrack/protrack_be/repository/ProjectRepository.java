package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {}
