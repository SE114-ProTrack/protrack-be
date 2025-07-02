package com.protrack.protrack_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.protrack.protrack_be.dto.request.ProjectRequest;
import com.protrack.protrack_be.dto.response.ProjectResponse;

import com.protrack.protrack_be.dto.response.TaskResponse;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.service.ProjectService;
import com.protrack.protrack_be.service.impl.FileStorageService;
import com.protrack.protrack_be.validation.CreateGroup;
import com.protrack.protrack_be.validation.UpdateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.Validator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project", description = "API dự án")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private org.springframework.validation.SmartValidator validator;

    @Operation(summary = "Lấy tất cả dự án")
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy dự án theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectResponse> createProject(
            @Parameter(
                    description = "Thông tin dự án ở dạng JSON",
                    required = true,
                    example = """
                    {
                      "projectName": "Tên dự án mẫu",
                      "description": "Nội dung mô tả"
                    }
                    """
            )
            @RequestPart("project") String rawProjectJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        ObjectMapper mapper = new ObjectMapper();
        ProjectRequest request;

        try {
            request = mapper.readValue(rawProjectJson, ProjectRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Dữ liệu project không hợp lệ", e);
        }

        BindingResult result = new BeanPropertyBindingResult(request, "projectRequest");
        validator.validate(request, result, CreateGroup.class);
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getAllErrors().get(0).getDefaultMessage());
        }

        if (file != null && !file.isEmpty()) {
            String bannerUrl = fileStorageService.store(file);
            request.setBannerUrl(bannerUrl);
        }

        ProjectResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật dự án (kèm ảnh banner)")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable UUID id,
            @RequestPart("project") String rawProjectJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        ObjectMapper mapper = new ObjectMapper();
        ProjectRequest request;

        try {
            request = mapper.readValue(rawProjectJson, ProjectRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Dữ liệu JSON không hợp lệ", e);
        }

        BindingResult result = new BeanPropertyBindingResult(request, "projectRequest");
        validator.validate(request, result, UpdateGroup.class);
        if (result.hasErrors()) {
            throw new IllegalArgumentException(result.getAllErrors().get(0).getDefaultMessage());
        }

        if (file != null && !file.isEmpty()) {
            String bannerUrl = fileStorageService.store(file);
            request.setBannerUrl(bannerUrl);
        }

        ProjectResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Xóa dự án")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Xóa thành công");
    }

    @Operation(summary = "Lấy tất cả dự án theo user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUser(@PathVariable UUID userId){
        List<ProjectResponse> responses = service.getProjectsByUser(userId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Lấy 3 dự án tạo gần nhất theo user")
    @GetMapping("/user/{userId}/get3")
    public ResponseEntity<?> getTop3ByUser(@PathVariable UUID userId){
        List<ProjectResponse> responses = service.get3ByUser(userId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping(value = "/{id}/uploadBanner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Thay đổi ảnh bìa dự án")
    public ResponseEntity<String> uploadProjectBanner(
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile file) {
        String fileUrl = fileStorageService.store(file);
        ProjectResponse projectResponse = service.updateProjectBanner(id, fileUrl);
        return ResponseEntity.ok(fileUrl);
    }

}
