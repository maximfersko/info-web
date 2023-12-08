package com.fersko.info.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FriendDto implements BaseDto<Long> {

    private Long id;
    private PeerDto firstPeer;
    private PeerDto secondPeer;

}
