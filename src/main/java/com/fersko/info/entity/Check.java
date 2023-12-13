package com.fersko.info.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Check implements BaseEntity {

    private Long id;
    private Peer peer;
    private Task task;
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Check check)) return false;
        return Objects.equals(id, check.id)
               && Objects.equals(peer, check.peer)
               && Objects.equals(task, check.task)
               && Objects.equals(date, check.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, peer, task, date);
    }
}
