package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.PersonalProductivityRequest;
import com.protrack.protrack_be.dto.response.PersonalProductivityResponse;
import com.protrack.protrack_be.model.id.PersonalProductivityId;
import com.protrack.protrack_be.repository.PersonalProductivityRepository;
import com.protrack.protrack_be.service.PersonalProductivityService;
import com.protrack.protrack_be.service.UserService;
import com.protrack.protrack_be.validation.CreateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/personal-productivity")
@RequiredArgsConstructor
@Tag(name = "Personal Productivity", description = "API năng suất cá nhân")
public class PersonalProductivityController {

    @Autowired
    PersonalProductivityService service;

    @Autowired
    UserService userService;

    @Operation(summary = "Lấy tất cả năng suất cá nhân")
    @GetMapping
    public ResponseEntity<List<PersonalProductivityResponse>> getAllProductivity() {
        List<PersonalProductivityResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy năng suất cá nhân của người dùng hiện tại trong một dự án")
    @GetMapping("/{projectId}")
    public ResponseEntity<PersonalProductivityResponse> getProductivityByUserAndProject(
            @PathVariable UUID projectId) {
        PersonalProductivityId id = new PersonalProductivityId(userService.getCurrentUser().getUserId(), projectId);
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Tạo hoặc cập nhật năng suất cá nhân")
    @PostMapping
    public ResponseEntity<?> createOrUpdateProductivity(@RequestBody @Validated(CreateGroup.class) PersonalProductivityRequest request) {
        PersonalProductivityResponse response = service.save(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xem toàn bộ năng suất cá nhân của người dùng hiện tại")
    @GetMapping("/my")
    public ResponseEntity<List<PersonalProductivityResponse>> getMyProductivity() {
        List<PersonalProductivityResponse> responses = service.getAllOfCurrentUser();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Xem toàn bộ năng suất cá nhân của những thành viên trong một dự án")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<PersonalProductivityResponse>> getAllProductivityByProject(@PathVariable UUID projectId) {
        List<PersonalProductivityResponse> responses = service.getAllByProject(projectId);
        return ResponseEntity.ok(responses);
    }
}
