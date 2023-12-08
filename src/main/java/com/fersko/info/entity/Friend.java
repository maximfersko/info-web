package com.fersko.info.entity;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Friend implements BaseEntity<Long> {

    private Long id;
    private Peer firstPeer;
    private Peer secondPeer;

}
