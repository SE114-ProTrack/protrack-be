package com.protrack.protrack_be.repository;

import com.protrack.protrack_be.model.Function;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FunctionRepository extends JpaRepository<Function, UUID> {
    Optional<Function> findByFunctionCode(String functionCode);
    List<Function> findByFunctionCodeIn(List<String> functionCodes);
}
