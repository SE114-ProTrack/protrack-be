package com.protrack.protrack_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAttachmentResponse {
    private UUID id;
    private UUID taskId;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private LocalDateTime uploadedAt;

    public String getPreviewUrl() {
        if (fileUrl.contains("drive.google.com")) {
            return "https://drive.google.com/file/d/" + extractId(fileUrl) + "/preview";
        } else if (fileUrl.endsWith(".pdf") || fileUrl.contains("cloudinary")) {
            return fileUrl; // cloudinary đã hỗ trợ preview
        } else {
            return null; // fallback
        }
    }

    private String extractId(String url) {
        Pattern p = Pattern.compile("/d/([\\w-]+)");
        Matcher m = p.matcher(url);
        return m.find() ? m.group(1) : "";
    }

}

