package com.fersko.info.repository.impl;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Friend;
import com.fersko.info.entity.Peer;
import com.fersko.info.exceptions.ConnectionBDException;
import com.fersko.info.repository.FriendRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendRepositoryImpl implements FriendRepository {

    private static FriendRepositoryImpl friendRepositoryImpl;

    private static final String DELETE_SQL = """
            DELETE FROM friends WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO friends (peer1, peer2) VALUES (?, ?)
            """;

    private static final String FIND_ALL_SQL = """
                        SELECT
                            p.pk_nickname AS peer1_nickname,
                            p.birthday AS peer1_birthday,
                            p2.pk_nickname AS peer2_nickname,
                            p2.birthday AS peer2_birthday,
                            f.id
                        FROM friends f
                        JOIN peers p ON p.pk_nickname = f.peer1
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


    private FriendRepositoryImpl() {

    }

    public static FriendRepositoryImpl getFriendsRepository() {
        if (friendRepositoryImpl == null) {
            friendRepositoryImpl = new FriendRepositoryImpl();
        }
        return friendRepositoryImpl;
    }

    @Override
    public Optional<Friend> findById(Long id) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getFriend(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new ConnectionBDException("connection refused");
        }
    }

    @Override
    public void update(Friend entity) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getFirstPeer().getId());
            preparedStatement.setString(2, entity.getSecondPeer().getId());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ConnectionBDException("connection refused");
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ConnectionBDException("connection refused");
        }
    }


    @Override
    public Friend save(Friend entity) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getFirstPeer().getId());
            preparedStatement.setString(2, entity.getSecondPeer().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ConnectionBDException("connection refused");
        }
        return null;
    }

    @Override
    public List<Friend> findByAll() {
        try (Connection connection = ConnectionManager.getConnections();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            List<Friend> friends = null;
            if (resultSet != null) {
                friends = new ArrayList<>();
                while (resultSet.next()) {
                    friends.add(getFriend(resultSet));
                }
            }
            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Peer getFirstPeer(ResultSet resultSet) throws SQLException {
        return new Peer(
                resultSet.getString("peer1_nickname"),
                resultSet.getDate("peer1_birthday").toLocalDate()
        );
    }

    private Peer getSecondPeer(ResultSet resultSet) throws SQLException {
        return new Peer(
                resultSet.getString("peer2_nickname"),
                resultSet.getDate("peer2_birthday").toLocalDate()
        );
    }

    private Friend getFriend(ResultSet resultSet) throws SQLException {
        return new Friend(
                resultSet.getLong("id"),
                getFirstPeer(resultSet),
                getSecondPeer(resultSet)
        );
    }


}
