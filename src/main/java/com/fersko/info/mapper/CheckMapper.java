package com.fersko.info.mapper;

import com.fersko.info.dto.CheckDto;
import com.fersko.info.entity.Check;

public class CheckMapper implements BaseMapper<Check, CheckDto> {

    @Override
    public CheckDto toDto(Check entity) {
        return null;
    }

    @Override
    public Check toEntity(CheckDto dto) {
        return null;
    }
}
