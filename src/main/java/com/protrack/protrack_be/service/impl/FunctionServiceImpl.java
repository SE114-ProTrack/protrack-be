package com.protrack.protrack_be.service.impl;

import com.protrack.protrack_be.dto.request.FunctionRequest;
import com.protrack.protrack_be.dto.response.FunctionResponse;
import com.protrack.protrack_be.mapper.FunctionMapper;
import com.protrack.protrack_be.model.Function;
import com.protrack.protrack_be.repository.FunctionRepository;
import com.protrack.protrack_be.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.protrack.protrack_be.mapper.FunctionMapper.toEntity;
import static com.protrack.protrack_be.mapper.FunctionMapper.toResponse;

public class FunctionServiceImpl implements FunctionService {

    @Autowired
    FunctionRepository repo;

    @Override
    public List<FunctionResponse> getAll(){
        return repo.findAll().stream()
                .map(FunctionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FunctionResponse> getById(UUID id){
        return repo.findById(id)
                .map(FunctionMapper::toResponse);
    }

    @Override
    public FunctionResponse create(FunctionRequest request){
        Function function = toEntity(request);
        Function saved = repo.save(function);
        return toResponse(saved);
    }

    @Override
    public FunctionResponse update(UUID id, FunctionRequest request){
        Function function = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chức năng"));

        function.setFuctionName(request.getFuctionName());
        function.setScreenName(request.getScreenName());

        Function saved = repo.save(function);
        return toResponse(saved);
    }

    @Override
    public void delete(UUID id){ repo.deleteById(id); }
}
