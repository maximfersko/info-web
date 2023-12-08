package com.fersko.info.entity;


import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Check implements BaseEntity<Long> {

    private Long id;
    private Peer peer;
    private Task task;
    private LocalDate date;

}
