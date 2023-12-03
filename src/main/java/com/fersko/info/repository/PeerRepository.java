package com.fersko.info.repository;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Peer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PeerRepository implements BaseRepository<Peer, String> {

    private static PeerRepository peerRepository;

    private static final String DELETE_SQL = """
        DELETE FROM peers WHERE pk_nickname = ?
    """;

    private static final String SAVE_SQL = """
        INSERT INTO peers (pk_nickname, birthday) VALUES (?, ?)
    """;

    private static final String FIND_ALL_SQL = """
        SELECT p.pk_nickname, p.birthday FROM peers p
    """;

    private static final String FIND_BY_ID = FIND_ALL_SQL + """
        WHERE pk_nickname = ?
    """;

    private static final String UPDATE_SQL = """
        UPDATE peers
        SET birthday = ?
        WHERE pk_nickname = ?
    """;

    private  PeerRepository() {

    }

    public static PeerRepository getPeerRepository() {
        if (peerRepository == null) {
            peerRepository = new PeerRepository();
        }
        return peerRepository;
    }

    @Override
    public Optional<Peer> findById(String id) {
        try(Connection connection = ConnectionManager.getConnections();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)){
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Peer peer = getPeer(resultSet);
                return Optional.of(peer);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Peer entity) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)){
            preparedStatement.setDate(1, Date.valueOf(entity.getBirthday()));
            preparedStatement.setString(2, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)){
            preparedStatement.setString(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Peer save(Peer entity) {
        try (Connection connection = ConnectionManager.getConnections();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)){
            preparedStatement.setString(1, entity.getId());
            preparedStatement.setDate(2, Date.valueOf(entity.getBirthday()));
            preparedStatement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Peer> findByAll() {
        try (Connection connection = ConnectionManager.getConnections();
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            List<Peer> peers = null;
            if (resultSet != null) {
                 peers = new ArrayList<>();
                while (resultSet.next()) {
                    Peer peer = getPeer(resultSet);
                    peers.add(peer);
                }
            }
            return peers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Peer getPeer(ResultSet resultSet) throws SQLException {
        return new Peer(
                resultSet.getString("pk_nickname"),
                resultSet.getDate("birthday").toLocalDate()
        );
    }
}
