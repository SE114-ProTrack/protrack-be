package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.MessageRequest;
import com.protrack.protrack_be.dto.response.MessagePreviewResponse;
import com.protrack.protrack_be.dto.response.MessageResponse;
import com.protrack.protrack_be.service.MessageService;
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
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Message", description = "API tin nhắn")
public class MessageController {

    @Autowired
    MessageService service;

    @Operation(summary = "Lấy tất cả tin nhắn")
    @GetMapping
    public ResponseEntity<List<MessageResponse>> getAllMessages() {
        List<MessageResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy tin nhắn theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getMessageById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lấy chi tiết cuộc trò chuyện")
    @GetMapping("/with/{userId}")
    public ResponseEntity<List<MessageResponse>> getConversationWithUser(@PathVariable UUID userId) {
        List<MessageResponse> responses = service.getConversation(userId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Gửi tin nhắn")
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody @Valid MessageRequest request) {
        MessageResponse response = service.sendMessage(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật tin nhắn")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable UUID id, @RequestBody @Valid MessageRequest request) {
        MessageResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xóa tin nhắn")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }

    @Operation(summary = "Lấy danh sách cuộc trò chuyện")
    @GetMapping("/previews")
    public ResponseEntity<?> getMessagePreviews(){
        List<MessagePreviewResponse> responses = service.getPreviews();
        return ResponseEntity.ok(responses);
    }
}
