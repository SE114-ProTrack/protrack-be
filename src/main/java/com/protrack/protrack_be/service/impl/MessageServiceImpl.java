package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.MessageRequest;
import com.protrack.protrack_be.dto.response.MessagePreviewResponse;
import com.protrack.protrack_be.dto.response.MessageResponse;
import com.protrack.protrack_be.exception.AccessDeniedException;
import com.protrack.protrack_be.exception.BadRequestException;
import com.protrack.protrack_be.exception.NotFoundException;
import com.protrack.protrack_be.mapper.MessageMapper;
import com.protrack.protrack_be.model.Message;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.repository.MessageRepository;
import com.protrack.protrack_be.service.MessageService;
import com.protrack.protrack_be.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.MessageMapper.toResponse;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository repo;

    @Autowired
    UserService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final int FLOOD_LIMIT = 5; // Cho phép gửi tối đa 5 tin
    private static final int FLOOD_SECONDS = 10; // Trong 10 giây

    @Override
    @EnableSoftDeleteFilter
    public List<MessageResponse> getAll(){
        return repo.findAll().stream()
                .map(MessageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @EnableSoftDeleteFilter
    public Optional<MessageResponse> getById(UUID id){
        return repo.findById(id)
                .map(MessageMapper::toResponse);
    }

    @Override
    @EnableSoftDeleteFilter
    public Page<MessageResponse> getConversation(UUID userId, Pageable pageable) {
        UUID currentUserId = userService.getCurrentUser().getUserId();
        if (!currentUserId.equals(userId) && !repo.existsConversationBetween(currentUserId, userId)) {
            throw new AccessDeniedException("You are not permitted to view this conversation.");
        }

        return repo.findMessagesBetweenUsers(userId, userService.getCurrentUser().getUserId(), pageable)
                .map(MessageMapper::toResponse);
    }

    @Transactional
    @Override
    public MessageResponse sendMessage(MessageRequest request){
        User currentUser = userService.getCurrentUser();

        if (request.getReceiverId().equals(currentUser.getUserId())) {
            throw new BadRequestException("You cannot send a message to yourself.");
        }
        checkFlood(currentUser.getUserId(), request.getReceiverId());

        Message message = new Message();
        message.setSender(currentUser);
        message.setReceiver(userService.getUserById(request.getReceiverId())
                .orElseThrow(() -> new NotFoundException("Can not find receiver")));
        validateContent(request.getContent());
        message.setContent(request.getContent());
        message.setUpdatedAt(null);

        MessageResponse response = toResponse(repo.save(message));

        messagingTemplate.convertAndSend("/topic/user." + request.getReceiverId(), response);

        return response;
    }

    @Override
    @EnableSoftDeleteFilter
    public MessageResponse update(UUID id, MessageRequest request){
        Message message = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Can not find message"));
        if(!isValidUser(message.getSender().getUserId())) throw new AccessDeniedException("User does not have permission to update");
        if(message.getReceiver().getUserId().equals(request.getReceiverId()))
            throw new BadRequestException("Receiver do not match");

        validateContent(request.getContent());
        message.setContent(request.getContent());
        message.setUpdatedAt(LocalDateTime.now());

        Message saved = repo.save(message);
        return toResponse(saved);
    }
    @Override
    @EnableSoftDeleteFilter
    public void markAsRead(UUID id){
        Message message = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Can not find message"));
        User current = userService.getCurrentUser();
        if (!message.getReceiver().getUserId().equals(current.getUserId())) {
            throw new AccessDeniedException("You are not permitted to mark this message as read.");
        }

        message.setRead(true);
        message.setReadAt(LocalDateTime.now());

        Message saved = repo.save(message);
    }

    @Override
    public void delete(UUID id){
        Message message = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Can not find message"));

        if (!message.getSender().getUserId().equals(userService.getCurrentUser().getUserId())) {
            throw new AccessDeniedException("Only the sender can delete this message.");
        }

        repo.deleteById(id);
    }

    @Override
    public List<MessagePreviewResponse> getPreviews() {
        User current = userService.getCurrentUser();
        return repo.findPreviewsByUserId(current.getUserId());
    }

    @Override
    public Page<MessageResponse> searchMessages(String keyword, Pageable pageable) {
        UUID userId = userService.getCurrentUser().getUserId();
        return repo.searchByUserAndContent(userId, keyword, pageable)
                .map(MessageMapper::toResponse);
    }

    // HELPERS

    // if the current user is the message sender
    private boolean isValidUser(UUID userId){
        User currentUser = userService.getCurrentUser();
        return userId.equals(currentUser.getUserId());
    }

    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BadRequestException("Message content must not be empty.");
        }
        if (content.length() > 2000) {
            throw new BadRequestException("Message is too long (max 2000 characters).");
        }
    }

    private void checkFlood(UUID senderId, UUID receiverId) {
        String key = "msgflood:" + senderId + ":" + receiverId;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == null) count = 1L;
        if (count == 1L) {
            redisTemplate.expire(key, Duration.ofSeconds(FLOOD_SECONDS));
        }
        if (count > FLOOD_LIMIT) {
            throw new BadRequestException("You are sending messages too fast. Please wait a moment!");
        }
    }
}
