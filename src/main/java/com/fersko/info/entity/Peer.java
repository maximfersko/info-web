package com.fersko.info.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;


@Data
@AllArgsConstructor
public class Peer implements BaseEntity<String> {

    private String pkNickname;
    private LocalDate birthday;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Peer)) return false;
        Peer peer = (Peer) o;
        return Objects.equals(pkNickname, peer.pkNickname) && Objects.equals(birthday, peer.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkNickname, birthday);
    }

    @Override
    public String getId() {
        return pkNickname;
    }
}
