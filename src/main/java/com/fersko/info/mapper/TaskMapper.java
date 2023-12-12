package com.fersko.info.mapper;

import com.fersko.info.dto.TaskDto;
import com.fersko.info.entity.Task;

public class TaskMapper implements BaseMapper<Task, TaskDto> {

    @Override
    public TaskDto toDto(Task entity) {
        if (entity == null) {
            return null;
        }

        return TaskDto.builder()
                .id(entity.getId())
                .pkTitle(entity.getPkTitle())
                .parentTask(toDto(entity.getParentTask()))
                .maxXp(entity.getMaxXp())
                .build();
    }

    @Override
    public Task fromDto(TaskDto dto) {
        if (dto == null) {
            return null;
        }

        return Task.builder()
                .id(dto.getId())
                .pkTitle(dto.getPkTitle())
                .parentTask(fromDto(dto.getParentTask()))
                .maxXp(dto.getMaxXp())
                .build();
    }
}