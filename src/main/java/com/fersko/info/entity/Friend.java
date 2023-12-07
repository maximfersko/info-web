package com.fersko.info.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Friend implements BaseEntity<Long> {

    private Long id;
    private Peer firstPeer;
    private Peer secondPeer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friend)) return false;
        Friend friends = (Friend) o;
        return Objects.equals(id, friends.id) && Objects.equals(firstPeer, friends.firstPeer) && Objects.equals(secondPeer, friends.secondPeer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstPeer, secondPeer);
    }
}