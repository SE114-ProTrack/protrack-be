package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.ActivityHistoryRequest;
import com.protrack.protrack_be.dto.response.ActivityHistoryResponse;
import com.protrack.protrack_be.model.ActivityHistory;
import com.protrack.protrack_be.service.ActivityHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/activity-history")
@RequiredArgsConstructor
public class ActivityHistoryController {

    @Autowired
    ActivityHistoryService service;

    @GetMapping
    public ResponseEntity<List<ActivityHistoryResponse>> getAllActivityHistory() {
        List<ActivityHistoryResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityHistoryResponse> getActivityHistoryById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createActivityHistory(@RequestBody @Valid ActivityHistoryRequest request) {
        ActivityHistoryResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }
}
