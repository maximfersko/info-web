package com.fersko.info.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;


@Data
@AllArgsConstructor
public class Peer implements BaseEntity<String> {

    private String id;
    private LocalDate birthday;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Peer peer)) return false;
        return Objects.equals(id, peer.id) && Objects.equals(birthday, peer.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, birthday);
    }
}
