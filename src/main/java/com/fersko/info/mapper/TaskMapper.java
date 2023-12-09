package com.fersko.info.mapper;

import com.fersko.info.dto.TaskDto;
import com.fersko.info.entity.Task;

public class TaskMapper implements BaseMapper<Task, TaskDto> {

    @Override
    public TaskDto toDto(Task entity) {
        if (entity == null) {
            return null;
        }

        return new TaskDto(
                entity.getId(),
                toDto(entity.getParentTask()),
                entity.getMaxXp()
        );
    }

    @Override
    public Task toEntity(TaskDto dto) {
        if (dto == null) {
            return null;
        }

        return new Task(
                dto.getId(),
                toEntity(dto.getParentTask()),
                dto.getMaxXp()
        );
    }
}