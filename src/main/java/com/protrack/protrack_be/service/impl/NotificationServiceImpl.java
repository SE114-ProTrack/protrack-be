package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.NotificationRequest;
import com.protrack.protrack_be.dto.response.NotificationResponse;
import com.protrack.protrack_be.mapper.NotificationMapper;
import com.protrack.protrack_be.model.Notification;
import com.protrack.protrack_be.model.User;
import com.protrack.protrack_be.repository.NotificationRepository;
import com.protrack.protrack_be.service.NotificationService;
import com.protrack.protrack_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<NotificationResponse> getAll(UUID userId){
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Cannot find user"));
        return repo.findByReceiver(user)
                .stream()
                .map(NotificationMapper::toResponse)
                .collect(Collectors.toList());
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
                .orElseThrow(() -> new RuntimeException("Can not find receiver"));

        noti.setReceiver(receiver);
        noti.setType(request.getType());
        noti.setContent(request.getContent());
        noti.setTimestamp(LocalDateTime.now());

        Notification saved = repo.save(noti);

        return toResponse(saved);
    }

    @Override
    public void markAsRead(UUID id){
        Notification noti = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not find notification"));

        noti.setIsRead(true);
        repo.save(noti);
    }

    @Override
    public void delete(UUID id){
        repo.deleteById(id);
    }
}
