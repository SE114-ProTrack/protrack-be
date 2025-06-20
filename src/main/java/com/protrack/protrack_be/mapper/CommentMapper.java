package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.CommentResponse;
import com.protrack.protrack_be.model.Comment;

public class CommentMapper {

    public static CommentResponse toResponse(Comment entity) {
        return new CommentResponse(
                entity.getCommentId(),
                entity.getTask().getTaskId(),
                entity.getUser().getUserId(),
                entity.getUser().getName(),
                entity.getContent(),
                entity.getTimestamp()
        );
    }
}
