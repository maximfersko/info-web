package com.fersko.info.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class TaskDto implements BaseDto<String> {

    private String pkTitle;
    private TaskDto parentTask;
    private Integer maxXp;

    public TaskDto() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskDto)) return false;
        TaskDto taskDto = (TaskDto) o;
        return Objects.equals(pkTitle, taskDto.pkTitle) && Objects.equals(parentTask, taskDto.parentTask) && Objects.equals(maxXp, taskDto.maxXp);
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
