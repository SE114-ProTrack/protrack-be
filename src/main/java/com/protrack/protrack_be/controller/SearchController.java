package com.protrack.protrack_be.controller;

import com.protrack.protrack_be.dto.response.SearchResponse;
import com.protrack.protrack_be.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Tìm kiếm dự án/công việc")
public class SearchController {
    @Autowired
    SearchService service;

    @Operation(summary = "Tìm kiếm dự án/công việc")
    @GetMapping("/{type}/{keyword}")
    public ResponseEntity<?> search(@PathVariable String type, @PathVariable String keyword){
        List<SearchResponse> responses = service.search(type, keyword);
        return ResponseEntity.ok(responses);
    }
}
