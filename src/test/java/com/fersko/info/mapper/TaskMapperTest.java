package com.fersko.info.mapper;

import com.fersko.info.dto.TaskDto;
import com.fersko.info.entity.Task;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class TaskMapperTest {

    private final TaskMapper taskMapper = new TaskMapper();

    @Test
    void testToDtoWithNonNullValues() {
        Task task = createSampleTask();
        TaskDto taskDto = taskMapper.toDto(task);

        assertNotNull(taskDto);
        assertEquals(task.getId(), taskDto.getId());
        assertNull(taskDto.getParentTask());
        assertEquals(task.getMaxXp(), taskDto.getMaxXp());
    }

    @Test
    void testToEntityWithNonNullValues() {
        TaskDto taskDto = createSampleTaskDto();
        Task task = taskMapper.toEntity(taskDto);

        assertNotNull(task);
        assertEquals(taskDto.getId(), task.getId());
        assertNull(task.getParentTask());
        assertEquals(taskDto.getMaxXp(), task.getMaxXp());
    }

    private Task createSampleTask() {
        return new Task("linux", null, 100);
    }

    private TaskDto createSampleTaskDto() {
        return new TaskDto("linux", null, 100);
    }


}
