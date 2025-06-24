package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.response.MessageResponse;

import java.util.List;

public interface MessageService {
    List<MessageResponse> getAllMessages();
    MessageResponse getMessageById();
}
