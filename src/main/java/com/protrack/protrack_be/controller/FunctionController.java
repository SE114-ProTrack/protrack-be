package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.response.FunctionResponse;
import com.protrack.protrack_be.mapper.FunctionMapper;
import com.protrack.protrack_be.model.Function;
import com.protrack.protrack_be.repository.FunctionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/functions")
@RequiredArgsConstructor
public class FunctionController {

    private final FunctionRepository functionRepository;

    @GetMapping
    public ResponseEntity<List<FunctionResponse>> getAllFunctions() {
        List<Function> functions = functionRepository.findAll();
        List<FunctionResponse> responses = functions.stream()
                .map(FunctionMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FunctionResponse> getFunctionById(@PathVariable Long id) {
        return functionRepository.findById(id)
                .map(FunctionMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createFunction(@RequestBody Object request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFunction(@PathVariable Long id, @RequestBody Object request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFunction(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
