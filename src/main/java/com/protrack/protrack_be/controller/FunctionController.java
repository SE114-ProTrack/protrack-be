package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.FunctionRequest;
import com.protrack.protrack_be.dto.response.FunctionResponse;

import com.protrack.protrack_be.service.FunctionService;
import com.protrack.protrack_be.validation.CreateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/functions")
@RequiredArgsConstructor
@Tag(name = "Function", description = "API chức năng")
public class FunctionController {

    @Autowired
    private FunctionService service;

    @Operation(summary = "Lấy toàn bộ chức năng")
    @GetMapping
    public ResponseEntity<List<FunctionResponse>> getAllFunctions() {
        List<FunctionResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy chức năng theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<FunctionResponse> getFunctionById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Tạo chức năng")
    @PostMapping
    public ResponseEntity<?> createFunction(@Validated(CreateGroup.class) @RequestBody FunctionRequest request) {
        FunctionResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật chức năng")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFunction(@PathVariable UUID id, @RequestBody FunctionRequest request) {
        FunctionResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xóa chức năng")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFunction(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}
