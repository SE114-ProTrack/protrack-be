package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.PersonalProductivityRequest;
import com.protrack.protrack_be.dto.response.PersonalProductivityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/personal-productivity")
@RequiredArgsConstructor
public class PersonalProductivityController {

    @GetMapping
    public ResponseEntity<List<PersonalProductivityResponse>> getAllProductivity() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{userId}/{projectId}")
    public ResponseEntity<PersonalProductivityResponse> getProductivityByUserAndProject(
            @PathVariable UUID userId,
            @PathVariable UUID projectId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateProductivity(@RequestBody @Valid PersonalProductivityRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
