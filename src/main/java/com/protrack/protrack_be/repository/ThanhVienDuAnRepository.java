package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.ThanhVienDuAn;
import com.protrack.protrack_be.model.id.ThanhVienDuAnId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThanhVienDuAnRepository extends JpaRepository<ThanhVienDuAn, ThanhVienDuAnId> {}
