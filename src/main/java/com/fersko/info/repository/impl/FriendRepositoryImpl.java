package com.fersko.info.repository.impl;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Friend;
import com.fersko.info.entity.Peer;
import com.fersko.info.repository.FriendRepository;
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
public class FriendRepositoryImpl implements FriendRepository {
    private static final String DELETE_SQL = """
            DELETE FROM friends WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO friends (peer1, peer2) VALUES (?, ?)
            """;
    private static final String FIND_ALL_SQL = """
                        SELECT
                            f.id,
                            p1.id AS peer1_id,
                            p1.pk_nickname AS peer1_nickname,
                            p1.birthday AS peer1_birthday,
                            p2.id AS peer2_id,
                            p2.pk_nickname AS peer2_nickname,
                            p2.birthday AS peer2_birthday
                        FROM friends f
                        JOIN peers p1 ON p1.pk_nickname = f.peer1
                        JOIN peers p2 ON p2.pk_nickname = f.peer2
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL +
                                                 " WHERE f.id = ?";
    private static final String UPDATE_SQL = """
                      UPDATE friends  
                      SET peer1 = ?, 
                        peer2 = ?  
                      WHERE id = ?
            """;
    private final ConnectionManager connectionManager;

    public FriendRepositoryImpl() {
        this.connectionManager = new ConnectionManager();
    }

    public FriendRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<Friend> findById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getFriend(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Friend update(Friend entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getFirstPeer().getPkNickname());
            preparedStatement.setString(2, entity.getSecondPeer().getPkNickname());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return null;
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
    public Friend save(Friend entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getFirstPeer().getPkNickname());
            preparedStatement.setString(2, entity.getSecondPeer().getPkNickname());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        entity.setId(resultSet.getLong(1));
                    }
                }
            } else {
                log.error("Saving friend failed, no rows affected.");
            }

            return entity;
        } catch (SQLException e) {
            log.error("Error saving friend: {}", e.getMessage(), e);
        }

        return null;
    }


    @Override
    public List<Friend> findByAll() {
        List<Friend> friends = null;
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            if (resultSet.next()) {
                friends = new ArrayList<>();
                while (resultSet.next()) {
                    friends.add(getFriend(resultSet));
                }
            }
            return friends;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return friends;
    }

    private Peer getPeer(ResultSet resultSet, String prefix) throws SQLException {
        return new Peer(
                resultSet.getLong(prefix + "_id"),
                resultSet.getString(prefix + "_nickname"),
                resultSet.getDate(prefix + "_birthday").toLocalDate()
        );
    }

    private Friend getFriend(ResultSet resultSet) throws SQLException {
        return new Friend(
                resultSet.getLong("id"),
                getPeer(resultSet, "peer1"),
                getPeer(resultSet, "peer2")
        );
    }
}
