package com.fersko.info.mapper;

import com.fersko.info.dto.PeerDto;
import com.fersko.info.entity.Peer;

public class PeerMapper implements BaseMapper<Peer, PeerDto> {

    @Override
    public PeerDto toDto(Peer entity) {
        if (entity == null) {
            return null;
        }
        return new PeerDto(
                entity.getId(),
                entity.getPkNickname(),
                entity.getBirthday()
        );
    }

    @Override
    public Peer fromDto(PeerDto dto) {
        if (dto == null) {
            return null;
        }
        return new Peer(
                dto.getId(),
                dto.getPkNickname(),
                dto.getBirthday()
        );
    }
}

