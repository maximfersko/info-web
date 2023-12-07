package com.fersko.info.mapper;

import com.fersko.info.dto.CheckDto;
import com.fersko.info.dto.PeerDto;
import com.fersko.info.entity.Check;

public class CheckMapper implements BaseMapper<Check, CheckDto> {

    private static final PeerMapper peerMapper = new PeerMapper();
    private static final TaskMapper taskMapper = new TaskMapper();

    @Override
    public CheckDto toDto(Check entity) {
        if (entity == null) {
            return null;
        }

        PeerDto peerDto = peerMapper.toDto(entity.getPeer());

        String peerPkNickname = (peerDto != null) ? peerDto.getId() : null;

        return new CheckDto(
                entity.getId(),
                peerMapper.toDto(entity.getPeer()),
                taskMapper.toDto(entity.getTask()),
                entity.getDate()
        );
    }

    @Override
    public Check toEntity(CheckDto dto) {
        if (dto == null) {
            return null;
        }
        return new Check(
                dto.getId(),
                peerMapper.toEntity(dto.getPeerDto()),
                taskMapper.toEntity(dto.getTaskDto()),
                dto.getDate()
        );
    }
}

