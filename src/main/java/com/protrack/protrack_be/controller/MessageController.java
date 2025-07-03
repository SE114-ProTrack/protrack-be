package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.MessageRequest;
import com.protrack.protrack_be.dto.response.MessagePreviewResponse;
import com.protrack.protrack_be.dto.response.MessageResponse;
import com.protrack.protrack_be.exception.BadRequestException;
import com.protrack.protrack_be.service.MessageService;
import com.protrack.protrack_be.validation.CreateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.Duration;
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
    public ResponseEntity<Page<MessageResponse>> getConversationWithUser(@PathVariable UUID userId,
                                                                         @RequestParam int page,
                                                                         @RequestParam int size) {
        Page<MessageResponse> responses = service.getConversation(userId, PageRequest.of(page, size));
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
    public ResponseEntity<?> updateMessage(@PathVariable UUID id, @RequestBody @Validated(CreateGroup.class) MessageRequest request) {
        MessageResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xóa tin nhắn")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }

    @Operation(summary = "Tìm kiếm tin nhắn trong một cuộc trò chuyện với một user cụ thể")
    @GetMapping("/search-in-conversation")
    public ResponseEntity<Page<MessageResponse>> searchMessagesInConversation(
            @RequestParam UUID withUserId,
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<MessageResponse> result = service.searchMessagesInConversation(withUserId, keyword, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Tìm kiếm các cuộc trò chuyện theo tên người nhận")
    @GetMapping("/search-by-name")
    public ResponseEntity<List<MessagePreviewResponse>> searchConversationsByReceiverName(
            @RequestParam String keyword
    ) {
        List<MessagePreviewResponse> result = service.searchConversationsByName(keyword);
        return ResponseEntity.ok(result);
    }



    @Operation(summary = "Lấy danh sách cuộc trò chuyện")
    @GetMapping("/previews")
    public ResponseEntity<?> getMessagePreviews(){
        List<MessagePreviewResponse> responses = service.getPreviews();
        return ResponseEntity.ok(responses);
    }
}
