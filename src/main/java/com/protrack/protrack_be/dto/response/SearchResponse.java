package com.protrack.protrack_be.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private String type; // "project", "task", "all"
    private Object data;

    @JsonIgnore
    private LocalDateTime time;
}
