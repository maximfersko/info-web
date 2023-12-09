package com.fersko.info.mapper;

import com.fersko.info.dto.FriendDto;
import com.fersko.info.dto.PeerDto;
import com.fersko.info.entity.Friend;
import com.fersko.info.entity.Peer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FriendMapperTest {

    private final FriendMapper friendMapper = new FriendMapper();
    private final PeerMapper peerMapper = new PeerMapper();

    @Test
    void testToDtoWithNonNullValues() {
        Friend friend = createSampleFriend();
        FriendDto friendDto = friendMapper.toDto(friend);


        assertNotNull(friendDto);
        assertEquals(friend.getId(), friendDto.getId());
        assertNotNull(friendDto.getFirstPeer());
        assertNotNull(friendDto.getSecondPeer());
    }


    @Test
    void testToEntityWithNonNullValues() {
        FriendDto friendDto = createSampleFriendDto();
        Friend friend = friendMapper.toEntity(friendDto);

        assertNotNull(friend);
        assertEquals(friendDto.getId(), friend.getId());
        assertNotNull(friend.getFirstPeer());
        assertNotNull(friend.getSecondPeer());
    }


    private Friend createSampleFriend() {
        return new Friend(1L, peerMapper.toEntity(createSamplePeerDto()), peerMapper.toEntity(createSamplePeerDto()));
    }

    private FriendDto createSampleFriendDto() {
        return new FriendDto(1L, peerMapper.toDto(createSamplePeer()), peerMapper.toDto(createSamplePeer()));
    }

    private Peer createSamplePeer() {
        return new Peer("nickname", LocalDate.parse("2000-01-01"));
    }

    private PeerDto createSamplePeerDto() {
        return new PeerDto("nickname", LocalDate.parse("2000-01-01"));
    }

}
