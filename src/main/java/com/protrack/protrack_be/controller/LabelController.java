package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.LabelRequest;
import com.protrack.protrack_be.dto.response.LabelResponse;
import com.protrack.protrack_be.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Label", description = "API nhãn")
public class LabelController {

    @Autowired
    LabelService service;

    @Operation(summary = "Lấy tất cả các nhãn")
    @GetMapping
    public ResponseEntity<List<LabelResponse>> getAllLabels() {
        List<LabelResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy nhãn theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<LabelResponse> getLabelById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Tạo nhãn")
    @PostMapping
    public ResponseEntity<?> createLabel(@RequestBody @Valid LabelRequest request) {
        LabelResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật nhãn")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLabel(@PathVariable UUID id, @RequestBody @Valid LabelRequest request) {
        LabelResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xóa nhãn")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLabel(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
