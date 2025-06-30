package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.MessageRequest;
import com.protrack.protrack_be.dto.response.MessagePreviewResponse;
import com.protrack.protrack_be.dto.response.MessageResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    List<MessageResponse> getAll();
    Optional<MessageResponse> getById(UUID id);
    List<MessageResponse> getConversation(UUID user);
    MessageResponse sendMessage(MessageRequest request);
    MessageResponse update(UUID id, MessageRequest request);
    void markAsRead(UUID id);
    void delete(UUID id);
    List<MessagePreviewResponse> getPreviews();
}
