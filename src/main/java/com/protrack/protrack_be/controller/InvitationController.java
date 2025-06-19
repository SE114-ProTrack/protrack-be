package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.InvitationRequest;
import com.protrack.protrack_be.dto.response.InvitationResponse;

import com.protrack.protrack_be.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    @Autowired
    private InvitationService service;

    @GetMapping
    public ResponseEntity<List<InvitationResponse>> getAllInvitations() {
        List<InvitationResponse> responseList = service.getAll();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvitationResponse> getInvitationById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createInvitation(@RequestBody @Valid InvitationRequest request) {
        InvitationResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{token}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable String token) {
        InvitationResponse response = service.accept(token);
        return ResponseEntity.ok(response);
    }
}
