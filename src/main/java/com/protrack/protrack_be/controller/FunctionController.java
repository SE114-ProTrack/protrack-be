package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.FunctionRequest;
import com.protrack.protrack_be.dto.response.FunctionResponse;

import com.protrack.protrack_be.service.FunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/functions")
@RequiredArgsConstructor
public class FunctionController {

    @Autowired
    private FunctionService service;

    @GetMapping
    public ResponseEntity<List<FunctionResponse>> getAllFunctions() {
        List<FunctionResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FunctionResponse> getFunctionById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createFunction(@RequestBody FunctionRequest request) {
        FunctionResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFunction(@PathVariable UUID id, @RequestBody FunctionRequest request) {
        FunctionResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFunction(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
