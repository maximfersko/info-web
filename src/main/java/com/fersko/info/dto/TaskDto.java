package com.fersko.info.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class TaskDto implements BaseDto<String> {

    private String id;
    private TaskDto parentTask;
    private Integer maxXp;

    public TaskDto() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskDto taskDto)) return false;
        return Objects.equals(id, taskDto.id) && Objects.equals(parentTask, taskDto.parentTask) && Objects.equals(maxXp, taskDto.maxXp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentTask, maxXp);
    }
}
