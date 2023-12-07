package com.fersko.info.mapper;

import com.fersko.info.dto.BaseDto;
import com.fersko.info.entity.BaseEntity;

public interface BaseMapper<E extends BaseEntity, D extends BaseDto> {
    D toDto(E entity);

    E toEntity(D dto);
}
