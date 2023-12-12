package com.fersko.info.service.impl;

import com.fersko.info.dto.TaskDto;
import com.fersko.info.entity.Task;
import com.fersko.info.mapper.TaskMapper;
import com.fersko.info.repository.impl.TaskRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class TaskServiceImplTest {

    @Mock
    private TaskRepositoryImpl taskRepositoryMock;

    @Mock
    private TaskMapper taskMapperMock;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_shouldReturnOptionalTaskDto_whenIdExists() {
        Long taskId = 1L;
        TaskDto expectedTaskDto = createSampleTaskDto();

        when(taskRepositoryMock.findById(taskId)).thenReturn(Optional.of(createSampleTask()));
        when(taskMapperMock.toDto(any())).thenReturn(expectedTaskDto);

        Optional<TaskDto> result = taskService.findById(taskId);

        assertTrue(result.isPresent());
        assertEquals(expectedTaskDto, result.get());

        verify(taskRepositoryMock, times(1)).findById(taskId);
        verify(taskMapperMock, times(1)).toDto(any());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {
        Long nonExistentTaskId = 100L;

        when(taskRepositoryMock.findById(nonExistentTaskId)).thenReturn(Optional.empty());

        Optional<TaskDto> result = taskService.findById(nonExistentTaskId);

        assertTrue(result.isEmpty());

        verify(taskRepositoryMock, times(1)).findById(nonExistentTaskId);
        verifyNoInteractions(taskMapperMock);
    }

    @Test
    void update_shouldInvokeUpdateMethodInRepository() {
        TaskDto taskDtoToUpdate = createSampleTaskDto();
        taskService.update(taskDtoToUpdate);
        verify(taskRepositoryMock, times(1)).update(any());
    }

    @Test
    void delete_shouldReturnTrue_whenTaskIsDeleted() {
        Long taskIdToDelete = 1L;
        when(taskRepositoryMock.delete(taskIdToDelete)).thenReturn(true);
        boolean result = taskService.delete(taskIdToDelete);
        assertTrue(result);
        verify(taskRepositoryMock, times(1)).delete(taskIdToDelete);
    }

    @Test
    void delete_shouldReturnFalse_whenTaskIsNotDeleted() {
        Long taskIdToDelete = 1L;

        when(taskRepositoryMock.delete(taskIdToDelete)).thenReturn(false);

        boolean result = taskService.delete(taskIdToDelete);

        assertFalse(result);

        verify(taskRepositoryMock, times(1)).delete(taskIdToDelete);
    }

    @Test
    void save_shouldReturnSavedTaskDto() {
        TaskDto taskDtoToSave = createSampleTaskDto();

        when(taskRepositoryMock.save(any())).thenReturn(createSampleTask());
        when(taskMapperMock.toDto(any())).thenReturn(taskDtoToSave);

        TaskDto savedTaskDto = taskService.save(taskDtoToSave);

        assertNotNull(savedTaskDto);
        assertEquals(taskDtoToSave, savedTaskDto);

        verify(taskRepositoryMock, times(1)).save(any());
        verify(taskMapperMock, times(1)).toDto(any());
    }

    private Task createSampleTask() {
        return new Task(1L, "cpp_web_calc", null, 100);
    }

    private TaskDto createSampleTaskDto() {
        return new TaskDto(1L, "go_calc", null, 100);
    }
}
