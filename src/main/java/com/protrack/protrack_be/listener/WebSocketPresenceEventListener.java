package com.protrack.protrack_be.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.Duration;
import java.util.List;

@Component
public class WebSocketPresenceEventListener {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final long TTL_SECONDS = 60;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = getUserIdFromAccessor(accessor);
        if (userId != null) {
            redisTemplate.opsForValue().set("user:online:" + userId, "true", Duration.ofSeconds(TTL_SECONDS));
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = getUserIdFromAccessor(accessor);
        if (userId != null) {
            redisTemplate.delete("user:online:" + userId);
        }
    }

    private String getUserIdFromAccessor(StompHeaderAccessor accessor) {
        List<String> userIdHeader = accessor.getNativeHeader("userId");
        return (userIdHeader != null && !userIdHeader.isEmpty()) ? userIdHeader.get(0) : null;
    }
}

