package com.fersko.info.repository.impl;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskRepositoryImplTest {

    @Mock
    private ConnectionManager connectionManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private TaskRepositoryImpl taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_shouldReturnTask_whenIdExists() throws SQLException {
        String taskId = "testTask";
        Task expectedTask = createSampleTask();
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("pk_title")).thenReturn(taskId);
        when(resultSet.getString("parent_task_title")).thenReturn(expectedTask.getParentTask().getId());
        when(resultSet.getInt("p_task_xp")).thenReturn(expectedTask.getParentTask().getMaxXp());
        when(resultSet.getInt("max_xp")).thenReturn(expectedTask.getMaxXp());

        Optional<Task> result = taskRepository.findById(taskId);

        assertTrue(result.isPresent());
        assertEquals(expectedTask, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() throws SQLException {
        String taskId = "nonexistentTask";
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Task> result = taskRepository.findById(taskId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdate() throws SQLException {
        Task task = createSampleTask();
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        taskRepository.update(task);

        verify(preparedStatement).setString(eq(1), anyString());
        verify(preparedStatement).setInt(eq(2), anyInt());
        verify(preparedStatement).setString(eq(3), anyString());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void delete_shouldReturnTrue_whenTaskIsDeleted() throws SQLException {
        String taskId = "testTask";
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = taskRepository.delete(taskId);

        assertTrue(result);
    }

    @Test
    void delete_shouldReturnFalse_whenTaskIsNotDeleted() throws SQLException {
        String taskId = "nonexistentTask";
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = taskRepository.delete(taskId);

        assertFalse(result);
    }

    @Test
    void save_shouldReturnSavedTask() throws SQLException {
        Task taskToSave = createSampleTask();
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Task savedTask = taskRepository.save(taskToSave);

        assertNotNull(savedTask);
        assertEquals(taskToSave, savedTask);
    }


    private Task createSampleTask() {
        Task parentTask = new Task("parentTask", null, 50);
        return new Task("testTask", parentTask, 100);
    }
}
