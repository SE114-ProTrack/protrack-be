package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.ActivityHistoryRequest;
import com.protrack.protrack_be.dto.response.ActivityHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/activity-history")
@RequiredArgsConstructor
public class ActivityHistoryController {

    @GetMapping
    public ResponseEntity<List<ActivityHistoryResponse>> getAllActivityHistory() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityHistoryResponse> getActivityHistoryById(@PathVariable UUID id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping
    public ResponseEntity<?> createActivityHistory(@RequestBody @Valid ActivityHistoryRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
