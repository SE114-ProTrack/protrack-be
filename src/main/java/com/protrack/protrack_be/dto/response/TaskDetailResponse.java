package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailResponse {

    private UUID parentTaskId;
    private String parentTaskName;

    private UUID childTaskId;
    private String childTaskName;
}
