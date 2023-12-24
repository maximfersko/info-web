package com.fersko.info.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task extends BaseEntity {
    private Long id;
    private String pkTitle;
    private Task parentTask;
    private Integer maxXp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task))
            return false;
        return Objects.equals(id, task.id) &&
               Objects.equals(pkTitle, task.pkTitle) &&
               Objects.equals(parentTask, task.parentTask) &&
               Objects.equals(maxXp, task.maxXp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pkTitle, parentTask, maxXp);
    }
}
