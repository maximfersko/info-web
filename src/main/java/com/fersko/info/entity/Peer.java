package com.fersko.info.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Peer extends BaseEntity {
    private Long id;
    private String pkNickname;
    private LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Peer peer))
            return false;
        return Objects.equals(id, peer.id)
               && Objects.equals(pkNickname, peer.pkNickname)
               && Objects.equals(birthday, peer.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pkNickname, birthday);
    }
}
