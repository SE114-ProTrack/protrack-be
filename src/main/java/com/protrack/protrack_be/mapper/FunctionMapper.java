package com.protrack.protrack_be.mapper;

import com.protrack.protrack_be.dto.response.FunctionResponse;
import com.protrack.protrack_be.model.Function;

public class FunctionMapper {
    public static FunctionResponse toResponse(Function entity) {
        return new FunctionResponse(
                entity.getFunctionId(),
                entity.getFunctionCode(),
                entity.getFunctionName(),
                entity.getScreenName()
        );
    }
}
