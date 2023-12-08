package com.fersko.info.entity;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Task implements BaseEntity<String> {

    private String id;
    private Task parentTask;
    private Integer maxXp;

}
