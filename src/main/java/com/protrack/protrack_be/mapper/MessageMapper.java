package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.MessageResponse;
import com.protrack.protrack_be.model.Message;

public class MessageMapper {

    public static MessageResponse toResponse(Message entity) {
        return new MessageResponse(
                entity.getMessageId(),
                entity.getContent(),
                entity.getTimestamp(),
                entity.getProject().getProjectId(),
                entity.getProject().getProjectName(),
                entity.getSender().getUserId(),
                entity.getSender().getName()
        );
    }
}
