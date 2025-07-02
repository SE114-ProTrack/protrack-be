package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.annotation.EnableSoftDeleteFilter;
import com.protrack.protrack_be.dto.request.MessageRequest;
import com.protrack.protrack_be.dto.response.MessagePreviewResponse;
import com.protrack.protrack_be.dto.response.MessageResponse;
import com.protrack.protrack_be.mapper.MessageMapper;
import com.protrack.protrack_be.model.Message;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.repository.MessageRepository;
import com.protrack.protrack_be.service.MessageService;
import com.protrack.protrack_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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
    public List<MessageResponse> getConversation(UUID user) {
        return repo.findMessagesBetweenUsers(user, userService.getCurrentUser().getUserId()).stream()
                .map(MessageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponse sendMessage(MessageRequest request){
        Message message = new Message();
        User currentUser = userService.getCurrentUser();

        message.setSender(currentUser);
        message.setReceiver(userService.getUserById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Can not find receiver")));
        message.setContent(request.getContent());

        MessageResponse response = toResponse(repo.save(message));

        // send the message
        messagingTemplate.convertAndSend("/topic/user." + request.getReceiverId(), response);

        return response;
    }

    @Override
    @EnableSoftDeleteFilter
    public MessageResponse update(UUID id, MessageRequest request){
        Message message = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not find message"));
        if(!isVaidUser(message.getSender().getUserId())) throw new RuntimeException("User does not have permission to update");
        if(message.getReceiver().getUserId() != request.getReceiverId()) throw new RuntimeException("Receiver do not match");

        message.setContent(request.getContent());

        Message saved = repo.save(message);
        return toResponse(saved);
    }
    @Override
    @EnableSoftDeleteFilter
    public void markAsRead(UUID id){
        Message message = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not find message"));

        message.setRead(true);
        message.setReadAt(LocalDateTime.now());

        Message saved = repo.save(message);
    }

    @Override
    public void delete(UUID id){
        repo.deleteById(id);
    }

    @Override
    public List<MessagePreviewResponse> getPreviews() {
        User current = userService.getCurrentUser();
        return repo.findPreviewsByUserId(current.getUserId());
    }

    // HELPERS

    // if the current user is the message sender
    private boolean isVaidUser(UUID userId){
        User currentUser = userService.getCurrentUser();
        return userId == currentUser.getUserId();
    }
}
