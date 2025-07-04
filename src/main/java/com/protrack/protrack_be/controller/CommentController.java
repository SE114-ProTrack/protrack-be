package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.CommentRequest;
import com.protrack.protrack_be.dto.response.CommentResponse;
import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.service.CommentService;
import com.protrack.protrack_be.validation.CreateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "API bình luận")
public class CommentController {

    @Autowired
    private final CommentService service;

    @Operation(summary = "Lấy toàn bộ bình luận")
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(summary = "Lấy bình luận theo ID của task")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Page<CommentResponse>> getComments(@PathVariable UUID taskId,
                                                             @RequestParam int page,
                                                             @RequestParam int size) {
        Page<CommentResponse> responses = service.getCommentsByTask(taskId, PageRequest.of(page, size));
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy bình luận theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Tạo bình luận")
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody @Validated(CreateGroup.class) CommentRequest request) {
        CommentResponse response = service.createComment(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật bình luận")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable UUID id, @RequestBody @Valid CommentRequest request) {
        CommentResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xóa bình luận")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Delete comment successfully");
    }
}
