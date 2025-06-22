package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.LabelRequest;
import com.protrack.protrack_be.dto.response.LabelResponse;
import com.protrack.protrack_be.mapper.LabelMapper;
import com.protrack.protrack_be.model.Label;
import com.protrack.protrack_be.model.Project;
import com.protrack.protrack_be.repository.LabelRepository;
import com.protrack.protrack_be.service.LabelService;
import com.protrack.protrack_be.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.LabelMapper.toResponse;

public class LabelServiceImpl implements LabelService {

    @Autowired
    LabelRepository repo;

    @Autowired
    ProjectService projectService;

    @Override
    public List<LabelResponse> getAll(){
        return repo.findAll()
                .stream()
                .map(LabelMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LabelResponse> getById(UUID id){
        return repo.findById(id)
                .map(LabelMapper::toResponse);
    }

    @Override
    public LabelResponse create(LabelRequest request){
        Label label = new Label();
        Project project = projectService.getEntityById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Can not find project"));

        label.setLabelName(request.getLabelName());
        label.setDescription(request.getDescription());
        label.setProject(project);

        Label saved = repo.save(label);

        return toResponse(saved);
    }

    @Override
    public LabelResponse update(UUID id, LabelRequest request){
        Label label = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not find label"));

        if(request.getLabelName() != null) label.setLabelName(request.getLabelName());
        if(request.getDescription() != null) label.setDescription(request.getDescription());

        Label saved = repo.save(label);

        return toResponse(saved);
    }

    @Override
    public void delete(UUID id){
        repo.deleteById(id);
    }
}
