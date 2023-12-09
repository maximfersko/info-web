package com.fersko.info.mapper;

import com.fersko.info.dto.PeerDto;
import com.fersko.info.entity.Peer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PeerMapperTest {

    private final PeerMapper peerMapper = new PeerMapper();

    @Test
    void testToDtoWithNonNullValues() {
        Peer peer = createSamplePeer();
        PeerDto peerDto = peerMapper.toDto(peer);

        assertNotNull(peerDto);
        assertEquals(peer.getId(), peerDto.getId());
        assertEquals(peer.getBirthday(), peerDto.getBirthday());
    }


    @Test
    void testToEntityWithNonNullValues() {
        PeerDto peerDto = createSamplePeerDto();
        Peer peer = peerMapper.toEntity(peerDto);

        assertNotNull(peer);
        assertEquals(peerDto.getId(), peer.getId());
        assertEquals(peerDto.getBirthday(), peer.getBirthday());
    }


    private Peer createSamplePeer() {
        return new Peer("SampleNickname", LocalDate.parse("2000-01-01"));
    }

    private PeerDto createSamplePeerDto() {
        return new PeerDto("SampleNickname", LocalDate.parse("2000-01-01"));
    }


}
