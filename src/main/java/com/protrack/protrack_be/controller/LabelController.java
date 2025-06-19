package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.LabelRequest;
import com.protrack.protrack_be.dto.response.LabelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {

    @GetMapping
    public ResponseEntity<List<LabelResponse>> getAllLabels() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelResponse> getLabelById(@PathVariable UUID id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping
    public ResponseEntity<?> createLabel(@RequestBody @Valid LabelRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLabel(@PathVariable UUID id, @RequestBody @Valid LabelRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLabel(@PathVariable UUID id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
