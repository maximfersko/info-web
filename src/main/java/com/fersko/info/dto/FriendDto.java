package com.fersko.info.dto;

import com.fersko.info.entity.Peer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class FriendDto implements BaseDto<Long> {

    private Long id;
    private PeerDto firstPeer;
    private PeerDto secondPeer;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendDto)) return false;
        FriendDto friendDto = (FriendDto) o;
        return Objects.equals(id, friendDto.id) && Objects.equals(firstPeer, friendDto.firstPeer) && Objects.equals(secondPeer, friendDto.secondPeer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstPeer, secondPeer);
    }
}
