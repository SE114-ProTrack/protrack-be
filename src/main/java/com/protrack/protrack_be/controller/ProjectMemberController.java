package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.ProjectMemberRequest;
import com.protrack.protrack_be.dto.response.ProjectMemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-members")
@RequiredArgsConstructor
public class ProjectMemberController {

    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getAllProjectMembers() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{projectId}/{userId}")
    public ResponseEntity<ProjectMemberResponse> getProjectMemberById(@PathVariable Long projectId, @PathVariable Long userId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping
    public ResponseEntity<?> addProjectMember(@RequestBody @Valid ProjectMemberRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/{projectId}/{userId}")
    public ResponseEntity<?> updateProjectMember(@PathVariable Long projectId, @PathVariable Long userId, @RequestBody @Valid ProjectMemberRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{projectId}/{userId}")
    public ResponseEntity<?> deleteProjectMember(@PathVariable Long projectId, @PathVariable Long userId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
