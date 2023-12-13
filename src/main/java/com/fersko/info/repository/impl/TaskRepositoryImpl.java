package com.fersko.info.repository.impl;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Task;
import com.fersko.info.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TaskRepositoryImpl implements TaskRepository {

    private static final String DELETE_SQL =
            "DELETE FROM tasks WHERE id = ?";
    private static final String SAVE_SQL =
            "INSERT INTO tasks (pk_title, parent_task, max_xp) VALUES (?, ?, ?)";
    private static final String FIND_ALL_SQL = """
                    SELECT t.id, t.pk_title, t1.pk_title AS parent_task_title, t1.max_xp AS p_task_xp, t.max_xp
                    FROM tasks t
                    LEFT JOIN tasks t1 ON t.parent_task = t1.pk_title
            """;
    private static final String FIND_BY_ID_SQL =
            FIND_ALL_SQL + " WHERE t.id = ?";
    private static final String UPDATE_SQL =
            "UPDATE tasks SET parent_task = ?, max_xp = ? WHERE id = ?";
    private final ConnectionManager connectionManager;

    public TaskRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public TaskRepositoryImpl() {
        this.connectionManager = new ConnectionManager();
    }

    @Override
    public Optional<Task> findById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Task task = getTask(resultSet);
                return Optional.of(task);
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Task update(Task entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getParentTask().getPkTitle());
            preparedStatement.setInt(2, entity.getMaxXp());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return new Task();
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public Task save(Task entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getPkTitle());
            preparedStatement.setString(2, entity.getParentTask().getPkTitle());
            preparedStatement.setInt(3, entity.getMaxXp());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        entity.setId(resultSet.getLong(1));
                    }
                }
            } else {
                log.error("Saving task failed, no rows affected.");
            }

            return entity;
        } catch (SQLException e) {
            log.error("Error saving task: {}", e.getMessage(), e);
        }

        return new Task();
    }


    @Override
    public List<Task> findByAll() {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            List<Task> tasks = null;
            if (resultSet != null) {
                tasks = new ArrayList<>();
                while (resultSet.next()) {
                    tasks.add(getTask(resultSet));
                }
            }
            return tasks;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    private Task getParentTask(ResultSet resultSet) throws SQLException {
        String parentTaskTitle = resultSet.getString("parent_task_title");
        return (parentTaskTitle != null) ? new Task(
                resultSet.getLong("id"),
                parentTaskTitle,
                null,
                resultSet.getInt("p_task_xp")
        ) : null;
    }

    private Task getTask(ResultSet resultSet) throws SQLException {
        return Task.builder()
                .id(resultSet.getLong("id"))
                .pkTitle(resultSet.getString("pk_title"))
                .parentTask(getParentTask(resultSet))
                .maxXp(resultSet.getInt("max_xp"))
                .build();
    }
}
