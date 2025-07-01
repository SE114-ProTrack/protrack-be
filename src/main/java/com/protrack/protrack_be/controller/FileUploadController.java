package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.request.TaskAttachmentRequest;
import com.protrack.protrack_be.dto.response.TaskAttachmentResponse;
import com.protrack.protrack_be.service.TaskAttachmentService;
import com.protrack.protrack_be.service.impl.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "API quản lý tệp đính kèm")
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final TaskAttachmentService attachmentService;

    @PostMapping(value = "/tasks/{taskId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload file và gán vào task")
    public ResponseEntity<?> uploadAttachmentToTask(
            @PathVariable UUID taskId,
            @RequestPart("file") MultipartFile file) {

        String fileUrl = fileStorageService.store(file);

        TaskAttachmentRequest request = new TaskAttachmentRequest();
        request.setTaskId(taskId);
        request.setFileUrl(fileUrl);
        request.setFileName(file.getOriginalFilename());
        request.setFileType(file.getContentType());

        TaskAttachmentResponse savedAttachment = attachmentService.createTaskAttachment(request);
        return ResponseEntity.ok(savedAttachment);
    }


    @Operation(summary = "Lấy danh sách tệp đính kèm theo task")
    @GetMapping("/tasks/{taskId}/attachments")
    public ResponseEntity<List<TaskAttachmentResponse>> getAttachmentsByTask(@PathVariable UUID taskId) {
        List<TaskAttachmentResponse> attachments = attachmentService.getByTaskId(taskId);
        return ResponseEntity.ok(attachments);
    }

    @Operation(summary = "Xoá tệp đính kèm theo ID")
    @DeleteMapping("/attachments/{id}")
    public ResponseEntity<String> deleteAttachment(@PathVariable UUID id) {
        attachmentService.delete(id);
        return ResponseEntity.ok("Đã xoá file thành công");
    }
}

