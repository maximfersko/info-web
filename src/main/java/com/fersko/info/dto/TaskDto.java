package com.fersko.info.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto implements BaseDto<String> {

    private String id;
    private TaskDto parentTask;
    private Integer maxXp;


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
