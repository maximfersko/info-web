package com.fersko.info.repository.impl;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Task;
import com.fersko.info.repository.TaskRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository {

    private static final String DELETE_SQL =
            "DELETE FROM tasks WHERE pk_title = ?";
    private static final String SAVE_SQL =
            "INSERT INTO tasks (pk_title, parent_task, max_xp) VALUES (?, ?, ?)";
    private static final String FIND_ALL_SQL = """
                    SELECT t.pk_title, t1.pk_title AS parent_task_title, t1.max_xp AS p_task_xp, t.max_xp
                    FROM tasks t
                    LEFT JOIN tasks t1 ON t.parent_task = t1.pk_title
            """;
    private static final String FIND_BY_ID_SQL =
            FIND_ALL_SQL + " WHERE t.pk_title = ?";
    private static final String UPDATE_SQL =
            "UPDATE tasks SET parent_task = ?, max_xp = ? WHERE pk_title = ?";
    private final ConnectionManager connectionManager;

    public TaskRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public TaskRepositoryImpl() {
        this.connectionManager = new ConnectionManager();
    }

    @Override
    public Optional<Task> findById(String id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Task task = getTask(resultSet);
                return Optional.of(task);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Task entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getParentTask().getId());
            preparedStatement.setInt(2, entity.getMaxXp());
            preparedStatement.setString(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setString(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task save(Task entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, entity.getId());
            preparedStatement.setString(2, entity.getParentTask().getId());
            preparedStatement.setInt(3, entity.getMaxXp());
            preparedStatement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Task> findByAll() {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            List<Task> tasks = null;
            if (resultSet.next()) {
                tasks = new ArrayList<>();
                while (resultSet.next()) {
                    tasks.add(getTask(resultSet));
                }
            }
            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Task getParentTask(ResultSet resultSet) throws SQLException {
        String parentTaskTitle = resultSet.getString("parent_task_title");
        return (parentTaskTitle != null) ? new Task(
                parentTaskTitle,
                null,
                resultSet.getInt("p_task_xp")
        ) : null;
    }

    private Task getTask(ResultSet resultSet) throws SQLException {
        return new Task(
                resultSet.getString("pk_title"),
                getParentTask(resultSet),
                resultSet.getInt("max_xp")
        );
    }

}
