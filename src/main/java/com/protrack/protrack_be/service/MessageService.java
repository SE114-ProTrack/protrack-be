package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.request.MessageRequest;
import com.protrack.protrack_be.dto.response.MessagePreviewResponse;
import com.protrack.protrack_be.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    List<MessageResponse> getAll();
    Optional<MessageResponse> getById(UUID id);
    Page<MessageResponse> getConversation(UUID user, Pageable pageable);
    MessageResponse sendMessage(MessageRequest request);
    MessageResponse update(UUID id, MessageRequest request);
    void markAsRead(UUID id);
    void delete(UUID id);
    Page<MessageResponse> searchMessagesInConversation(UUID withUserId, String keyword, Pageable pageable);
    List<MessagePreviewResponse> searchConversationsByName(String keyword);
    List<MessagePreviewResponse> getPreviews();
}
