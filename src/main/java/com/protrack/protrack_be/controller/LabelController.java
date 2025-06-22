package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.LabelRequest;
import com.protrack.protrack_be.dto.response.LabelResponse;
import com.protrack.protrack_be.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {

    @Autowired
    LabelService service;

    @GetMapping
    public ResponseEntity<List<LabelResponse>> getAllLabels() {
        List<LabelResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelResponse> getLabelById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createLabel(@RequestBody @Valid LabelRequest request) {
        LabelResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLabel(@PathVariable UUID id, @RequestBody @Valid LabelRequest request) {
        LabelResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLabel(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
