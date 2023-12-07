package com.fersko.info.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Task implements BaseEntity<String> {

    private String pkTitle;
    private Task parentTask;
    private Integer maxXp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(pkTitle, task.pkTitle) && Objects.equals(parentTask, task.parentTask) && Objects.equals(maxXp, task.maxXp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkTitle, parentTask, maxXp);
    }

    @Override
    public String getId() {
        return pkTitle;
    }
}
