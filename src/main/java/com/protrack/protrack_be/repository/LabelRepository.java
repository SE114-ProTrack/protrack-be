package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LabelRepository extends JpaRepository<Label, UUID> {}
