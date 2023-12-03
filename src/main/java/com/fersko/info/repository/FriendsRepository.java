package com.fersko.info.repository;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Friends;
import com.fersko.info.entity.Peer;
import com.fersko.info.entity.Task;
import com.fersko.info.exceptions.ConnectionBDException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendsRepository implements BaseRepository<Friends, Integer> {

    private static FriendsRepository friendsRepository;

    private static final String DELETE_SQL = """
                DELETE FROM friends WHERE id = ?
            """;

    private static final String SAVE_SQL = """
                INSERT INTO friends (peer1, peer2) VALUES (?, ?)
            """;

    private static final String FIND_ALL_SQL = """
                SELECT p.pk_nickname, p.birthday, p2.pk_nickname, p2.birthday, f.id
                FROM friends f
                JOIN peers p ON p.pk_nickname = f.peer1
                JOIN peers p2 ON p2.pk_nickname = f.peer2           
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
                WHERE f.id = ?;
            """;

    private static final String UPDATE_SQL = """
                UPDATE friends
                SET peer1 = ?,
                peer2 = ?
                WHERE id = ?
            """;


    private FriendsRepository() {

    }

    public static FriendsRepository getFriendsRepository() {
        if (friendsRepository == null) {
            friendsRepository = new FriendsRepository();
        }
        return friendsRepository;
    }

    @Override
    public Optional<Friends> findById(Integer id) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
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
    public void update(Friends entity) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getFirstPeer().getPkNickname());
            preparedStatement.setString(2, entity.getSecondPeer().getPkNickname());
            preparedStatement.setInt(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ConnectionBDException("connection refused");
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ConnectionBDException("connection refused");
        }
    }

    @Override
    public Friends save(Friends entity) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getFirstPeer().getPkNickname());
            preparedStatement.setString(2, entity.getSecondPeer().getPkNickname());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ConnectionBDException("connection refused");
        }
        return null;
    }

    @Override
    public List<Friends> findByAll() {
        try (Connection connection = ConnectionManager.getConnections();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            List<Friends> friends = null;
            if (resultSet.next()) {
                friends = new ArrayList<>();
                while (resultSet.next()) {
                    friends.add(getFriend(resultSet));
                }
            }
            return friends;
        } catch (SQLException e) {
            throw new ConnectionBDException("connection refused");
        }
    }

    private Peer getFirstPeer(ResultSet resultSet) throws SQLException {
        return new Peer(
                resultSet.getString("p.pk_nickname"),
                resultSet.getDate("p.birthday").toLocalDate()
        );
    }

    private Peer getSecondPeer(ResultSet resultSet) throws SQLException {
        return new Peer(
                resultSet.getString("p2.pk_nickname"),
                resultSet.getDate("p2.birthday").toLocalDate()
        );
    }

    private Friends getFriend(ResultSet resultSet) throws SQLException {
        return new Friends(
                resultSet.getInt("f.id"),
                getFirstPeer(resultSet),
                getSecondPeer(resultSet)
        );
    }


}
