package com.fersko.info.mapper;

import com.fersko.info.dto.FriendDto;
import com.fersko.info.entity.Friend;

public class FriendMapper implements BaseMapper<Friend, FriendDto> {

    private static final PeerMapper peerMapper = new PeerMapper();


    @Override
    public FriendDto toDto(Friend entity) {
        if (entity == null) {
            return null;
        }
        return new FriendDto(
                entity.getId(),
                peerMapper.toDto(entity.getFirstPeer()),
                peerMapper.toDto(entity.getSecondPeer())
        );
    }

    @Override
    public Friend toEntity(FriendDto dto) {
        if (dto == null) {
            return null;
        }
        return new Friend(
                dto.getId(),
                peerMapper.toEntity(dto.getFirstPeer()),
                peerMapper.toEntity(dto.getSecondPeer())
        );
    }
}
