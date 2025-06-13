package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.InvitationRequest;
import com.protrack.protrack_be.dto.response.InvitationResponse;
import com.protrack.protrack_be.repository.InvitationRepository;
import com.protrack.protrack_be.mapper.InvitationMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationRepository loiMoiRepo;

    @GetMapping
    public ResponseEntity<List<InvitationResponse>> getAllInvitations() {
        List<InvitationResponse> responseList = loiMoiRepo.findAll().stream()
                .map(InvitationMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvitationResponse> getInvitationById(@PathVariable Long id) {
        return loiMoiRepo.findById(id)
                .map(InvitationMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createInvitation(@RequestBody @Valid InvitationRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PatchMapping("/{token}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable String token) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
