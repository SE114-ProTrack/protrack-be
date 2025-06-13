package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.PhanQuyenDuAn;
import com.protrack.protrack_be.model.id.PhanQuyenDuAnId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhanQuyenDuAnRepository extends JpaRepository<PhanQuyenDuAn, PhanQuyenDuAnId> {
}
