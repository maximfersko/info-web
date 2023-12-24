package com.fersko.info.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FriendDto extends BaseDto {
    private Long id;
    private PeerDto firstPeer;
    private PeerDto secondPeer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendDto friendDto))
            return false;
        return Objects.equals(id, friendDto.id)
               && Objects.equals(firstPeer, friendDto.firstPeer)
               && Objects.equals(secondPeer, friendDto.secondPeer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstPeer, secondPeer);
    }
}
