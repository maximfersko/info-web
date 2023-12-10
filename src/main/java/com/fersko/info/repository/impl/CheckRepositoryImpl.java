package com.fersko.info.repository.impl;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Check;
import com.fersko.info.entity.Peer;
import com.fersko.info.entity.Task;
import com.fersko.info.exceptions.ConnectionBDException;
import com.fersko.info.repository.CheckRepository;
import lombok.extern.slf4j.Slf4j;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CheckRepositoryImpl implements CheckRepository {

    private static final String DELETE_SQL = """
                DELETE FROM checks WHERE id = ?
            """;
    private static final String SAVE_SQL = """
                INSERT INTO checks (peer, task, date) VALUES (?, ?, ?)
            """;
    private static final String FIND_ALL_SQL = """
                SELECT
                    c.id,
                    c.date,
                    p.pk_nickname,
                    p.birthday,
                    t.pk_title,
                    t.max_xp,
                    pt.pk_title AS parent_pk_title,
                    pt.parent_task AS parent_parent_task,
                    pt.max_xp AS parent_max_xp
                FROM checks c
                JOIN public.peers p ON p.pk_nickname = c.peer
                JOIN public.tasks t ON t.pk_title = c.task
                LEFT JOIN public.tasks pt ON t.parent_task = pt.pk_title
            """;
    private static final String UPDATE_SQL = """
                UPDATE checks
                SET peer = ?,
                task = ?,
                date = ?
                WHERE id = ?;
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
                WHERE c.id = ?;
            """;
    private final ConnectionManager connectionManager;

    public CheckRepositoryImpl() {
        this.connectionManager = new ConnectionManager();
    }

    public CheckRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<Check> findById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Check check = getCheck(resultSet);
                return Optional.of(check);
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void update(Check entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getPeer().getId());
            preparedStatement.setString(2, entity.getTask().getId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getDate().atStartOfDay()));
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
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
    public Check save(Check entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getPeer().getId());
            preparedStatement.setString(2, entity.getTask().getId());
            preparedStatement.setDate(3, Date.valueOf(entity.getDate()));
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getLong("id"));
            }
            preparedStatement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return new Check();
    }

    @Override
    public List<Check> findByAll() {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            List<Check> checks = null;
            if (resultSet != null) {
                checks = new ArrayList<>();
                while (resultSet.next()) {
                    checks.add(getCheck(resultSet));
                }
            }
            return checks;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return  new ArrayList<>();
    }

    private Task getTask(ResultSet resultSet, Task parentTask) throws SQLException {
        return new Task(
                resultSet.getString("pk_title"),
                parentTask,
                resultSet.getInt("max_xp")
        );
    }

    private Peer getPeer(ResultSet resultSet) throws SQLException {
        return new Peer(
                resultSet.getString("pk_nickname"),
                resultSet.getDate("birthday").toLocalDate()
        );
    }

    private Task getParentTask(ResultSet resultSet) throws SQLException {
        String parentTaskTitle = resultSet.getString("parent_pk_title");
        return (parentTaskTitle != null) ? new Task(
                parentTaskTitle,
                null,
                resultSet.getInt("parent_max_xp")
        ) : null;
    }

    private Check getCheck(ResultSet resultSet) throws SQLException {
        Peer peer = getPeer(resultSet);
        Task parentTask = getParentTask(resultSet);
        Task currentTask = getTask(resultSet, parentTask);
        return new Check(
                resultSet.getLong("id"),
                peer,
                currentTask,
                resultSet.getDate("date").toLocalDate()
        );
    }

}
