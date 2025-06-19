package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.ActivityHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, UUID> {}

