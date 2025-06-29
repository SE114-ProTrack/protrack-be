package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.response.ProjectResponse;
import com.protrack.protrack_be.dto.response.SearchResponse;
import com.protrack.protrack_be.dto.response.TaskResponse;
import com.protrack.protrack_be.service.ProjectService;
import com.protrack.protrack_be.service.SearchService;
import com.protrack.protrack_be.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    ProjectService projectService;

    @Autowired
    TaskService taskService;

    @Override
    public List<SearchResponse> search(String type, String keyword) {
        List<SearchResponse> responses = new ArrayList<>();

        if(Objects.equals(type, "all") || Objects.equals(type, "project")){
            List<ProjectResponse> projectResponses = projectService.findByKeyword(keyword);
            for(ProjectResponse response : projectResponses){
                responses.add(new SearchResponse("project", response, response.getCreateTime()));
            }
        }
        if(Objects.equals(type, "all") || Objects.equals(type, "task")){
            List<TaskResponse> taskResponses = taskService.findByKeyword(keyword);
            for(TaskResponse response : taskResponses){
                responses.add(new SearchResponse("task", response, response.getCreatedTime()));
            }
        }

        responses.sort(Comparator.comparing(SearchResponse::getTime).reversed());

        return responses;
    }
}
