package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.PersonalProductivityRequest;
import com.protrack.protrack_be.dto.response.PersonalProductivityResponse;
import com.protrack.protrack_be.model.id.PersonalProductivityId;
import com.protrack.protrack_be.repository.PersonalProductivityRepository;
import com.protrack.protrack_be.service.PersonalProductivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/personal-productivity")
@RequiredArgsConstructor
public class PersonalProductivityController {

    @Autowired
    PersonalProductivityService service;

    @GetMapping
    public ResponseEntity<List<PersonalProductivityResponse>> getAllProductivity() {
        List<PersonalProductivityResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{userId}/{projectId}")
    public ResponseEntity<PersonalProductivityResponse> getProductivityByUserAndProject(
            @PathVariable UUID userId,
            @PathVariable UUID projectId) {
        PersonalProductivityId id = new PersonalProductivityId(userId, projectId);
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateProductivity(@RequestBody @Valid PersonalProductivityRequest request) {
        PersonalProductivityResponse response = service.save(request);
        return ResponseEntity.ok(response);
    }
}
