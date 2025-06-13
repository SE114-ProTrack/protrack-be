package com.protrack.protrack_be.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberResponse {

    private Long projectId;
    private Long userId;
    private String userFullName;
    private Boolean isProjectOwner;
}
