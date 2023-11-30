package com.fersko.info.repository;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Check;
import com.fersko.info.entity.Peer;
import com.fersko.info.entity.Task;
import com.fersko.info.exceptions.ConnectionBDException;

import java.sql.*;
import java.util.Optional;

public class ChecksRepository implements BaseRepository<Check, Integer> {
    private static final ChecksRepository INSTANCE = new ChecksRepository();

    private static final String DELETE = "DELETE FROM checks WHERE id = ?";

    private static final String SAVE = "INSERT INTO checks (id, peer, task, date) VALUES (?, ?, ?, ?)";

    private static final String FIND_ALL = """
        SELECT
            c.id,
            c.date,
            p.pk_nickname,
            p.birthday,
            t.pk_title,
            t.max_xp,
            parent.pk_title AS parent_pk_title,
            parent.parent_task AS parent_parent_task,
            parent.max_xp AS parent_max_xp
        FROM checks c
        JOIN public.peers p ON p.pk_nickname = c.peer
        JOIN public.tasks t ON t.pk_title = c.task
        LEFT JOIN public.tasks parent ON t.parent_task = parent.pk_title
        
    """;
    private static final String UPDATE = """
        UPDATE checks
        SET peer = ?,
        task = ?,
        date = ?
        WHERE id = ?;
    """;


    private static final String FIND_BY_ID = FIND_ALL + """
        WHERE c.id = ?;
    """;

    public static ChecksRepository getChecksRepository() {
        return INSTANCE;
    }


    @Override
    public Optional<Check> findById(Integer id) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Peer peer = new Peer(
                        resultSet.getString("pk_nickname"),
                        resultSet.getDate("birthday").toLocalDate()
                );
                Task parent_task  = new Task(
                        resultSet.getString("parent_pk_title"),
                        null,
                        resultSet.getInt("parent_max_xp")
                );
                Task task = new Task(
                        resultSet.getString("pk_title"),
                        parent_task,
                        resultSet.getInt("max_xp")
                );
                Check check = new Check(
                        resultSet.getInt("id"),
                        peer,
                        task,
                        resultSet.getDate("date").toLocalDate()
                );
                return Optional.of(check);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Check entity) {

    }

    @Override
    public boolean delete(Integer id) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ConnectionBDException("connection refused");
        }
    }

    @Override
    public Check save(Check entity) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.setString(2, entity.getPeer().getPkNickname());
            preparedStatement.setString(3, entity.getTask().getPkTitle());
            preparedStatement.setDate(4, Date.valueOf(entity.getDate()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }
}
