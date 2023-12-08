package com.fersko.info.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Peer implements BaseEntity<String> {

    private String id;
    private LocalDate birthday;

}
