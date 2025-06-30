package com.protrack.protrack_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserStatusController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/{userId}/status")
    public ResponseEntity<String> getUserStatus(@PathVariable String userId) {
        Boolean isOnline = redisTemplate.hasKey("user:online:" + userId);
        return ResponseEntity.ok(isOnline ? "online" : "offline");
    }
}

