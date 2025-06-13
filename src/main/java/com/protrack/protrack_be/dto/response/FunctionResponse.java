package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionResponse {
    private Long functionId;
    private String functionName;
    private String screenName;
}