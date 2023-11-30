package com.fersko.info.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Check {
    private Integer id;
    private Peer peer;
    private Task task;
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Check)) return false;
        Check check = (Check) o;
        return Objects.equals(id, check.id) && Objects.equals(peer, check.peer) && Objects.equals(task, check.task) && Objects.equals(date, check.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, peer, task, date);
    }
}
