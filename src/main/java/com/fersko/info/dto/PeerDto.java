package com.fersko.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
public class PeerDto implements BaseDto<String> {

    private String pkNickname;
    private LocalDate birthday;

    public PeerDto() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeerDto)) return false;
        PeerDto peerDto = (PeerDto) o;
        return Objects.equals(pkNickname, peerDto.pkNickname) && Objects.equals(birthday, peerDto.birthday);
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
