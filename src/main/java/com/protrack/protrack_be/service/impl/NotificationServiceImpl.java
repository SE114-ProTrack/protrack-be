package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.response.NotificationResponse;
import com.protrack.protrack_be.exception.AccessDeniedException;
import com.protrack.protrack_be.exception.NotFoundException;
import com.protrack.protrack_be.mapper.NotificationMapper;
import com.protrack.protrack_be.model.Notification;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.repository.NotificationRepository;
import com.protrack.protrack_be.service.NotificationService;
import com.protrack.protrack_be.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.NotificationMapper.toResponse;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository repo;

    @Autowired
    UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public Page<NotificationResponse> getAll(Pageable pageable) {
        User user = userService.getCurrentUser();
        return repo.findByReceiverOrderByTimestampDesc(user, pageable)
                .map(NotificationMapper::toResponse);
    }

    @Override
    public Optional<NotificationResponse> getById(UUID id){
        return repo.findById(id)
                .map(NotificationMapper::toResponse);
    }

    @Override
    public NotificationResponse create(NotificationRequest request){
        Notification noti = new Notification();
        User receiver = userService.getUserById(request.getReceiverId())
                .orElseThrow(() -> new NotFoundException("Can not find receiver"));

        noti.setReceiver(receiver);
        noti.setType(request.getType());
        noti.setContent(request.getContent());
        noti.setTimestamp(LocalDateTime.now());
        noti.setIsRead(false);
        noti.setActionUrl(request.getActionUrl());

        Notification saved = repo.save(noti);

        messagingTemplate.convertAndSend("/topic/notification." + saved.getReceiver().getUserId(), NotificationMapper.toResponse(saved));

        return NotificationMapper.toResponse(saved);
    }

    @Override
    public NotificationResponse markAsRead(UUID id){
        Notification noti = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Can not find notification"));

        UUID currentUserId = userService.getCurrentUser().getUserId();
        if (!noti.getReceiver().getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not permitted to mark this notification as read.");
        }

        noti.setIsRead(true);
        Notification saved = repo.save(noti);
        return NotificationMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id){
        Notification noti = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));

        UUID currentUserId = userService.getCurrentUser().getUserId();
        if (!noti.getReceiver().getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not permitted to delete this notification.");
        }

        repo.deleteById(id);
    }

    public long countUnread() {
        User user = userService.getCurrentUser();
        return repo.countByReceiverAndIsReadFalse(user);
    }

    @Transactional
    public int markAllAsRead() {
        UUID userId = userService.getCurrentUser().getUserId();
        return repo.markAllAsReadForUser(userId);
    }
}
