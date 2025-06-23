package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.PersonalProductivityRequest;
import com.protrack.protrack_be.dto.response.PersonalProductivityResponse;
import com.protrack.protrack_be.model.id.PersonalProductivityId;

import java.util.List;
import java.util.Optional;

public interface PersonalProductivityService {
    List<PersonalProductivityResponse> getAll();
    Optional<PersonalProductivityResponse> getById(PersonalProductivityId id);
    PersonalProductivityResponse save(PersonalProductivityRequest request);
}
