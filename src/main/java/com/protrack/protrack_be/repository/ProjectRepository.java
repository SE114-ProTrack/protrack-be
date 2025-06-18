package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {}
