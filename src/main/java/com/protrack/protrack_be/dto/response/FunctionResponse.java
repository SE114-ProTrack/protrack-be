package com.protrack.protrack_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionResponse {
    private UUID functionId;
    private String functionName;
    private String screenName;
}