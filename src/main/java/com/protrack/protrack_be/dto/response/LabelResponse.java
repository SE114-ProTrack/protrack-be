package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelResponse {

    private UUID labelId;
    private String labelName;
    private String description;
    private String color;

    private UUID projectId;
    private String projectName;
}
