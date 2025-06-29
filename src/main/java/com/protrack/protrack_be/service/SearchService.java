package com.protrack.protrack_be.service;

import com.protrack.protrack_be.dto.response.SearchResponse;

import java.util.List;

public interface SearchService {
    List<SearchResponse> search(String type, String keyword);
}
