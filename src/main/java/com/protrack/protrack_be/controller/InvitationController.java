package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.InvitationRequest;
import com.protrack.protrack_be.dto.response.InvitationResponse;
import com.protrack.protrack_be.service.InvitationService;
import com.protrack.protrack_be.validation.CreateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
@Tag(name = "Invitation", description = "API lời mời tham gia dự án")
public class InvitationController {

    private final InvitationService invitationService;

    @Operation(summary = "Lấy toàn bộ lời mời")
    @GetMapping
    public ResponseEntity<List<InvitationResponse>> getAll() {
        return ResponseEntity.ok(invitationService.getAll());
    }

    @Operation(summary = "Lấy lời mời theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<InvitationResponse> getById(@PathVariable UUID id) {
        return invitationService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Tạo lời mời (email hoặc notification)")
    @PostMapping
    public ResponseEntity<InvitationResponse> create(
            @RequestBody @Validated(CreateGroup.class) InvitationRequest req
    ) {
        InvitationResponse resp = invitationService.create(req);
        return ResponseEntity
                .status(201)
                .body(resp);
    }

    @Operation(summary = "Chấp nhận lời mời bằng token JWT")
    @PatchMapping("/accept")
    public ResponseEntity<InvitationResponse> accept(
            @RequestParam("token") String token
    ) {
        InvitationResponse resp = invitationService.accept(token);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Xóa lời mời (Admin/Owner only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        invitationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
