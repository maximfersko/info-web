package com.fersko.info.repository.impl;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Peer;
import com.fersko.info.repository.PeerRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// TODO: переделать исключения
public class PeerRepositoryImpl implements PeerRepository {

    private static final String DELETE_SQL =
            "DELETE FROM peers WHERE pk_nickname = ?";
    private static final String SAVE_SQL =
            "INSERT INTO peers (pk_nickname, birthday) VALUES (?, ?)";
    private static final String FIND_ALL_SQL =
            "SELECT p.pk_nickname, p.birthday FROM peers p";
    private static final String FIND_BY_ID =
            FIND_ALL_SQL + " WHERE pk_nickname = ?";
    private static final String UPDATE_SQL =
            "UPDATE peers SET birthday = ? WHERE pk_nickname = ?";
    private final ConnectionManager connectionManager;


    public PeerRepositoryImpl() {
        connectionManager = new ConnectionManager();
    }

    public PeerRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<Peer> findById(String id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
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
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setDate(1, Date.valueOf(entity.getBirthday()));
            preparedStatement.setString(2, entity.getId());
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
    public Peer save(Peer entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
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
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            List<Peer> peers = null;
            if (resultSet.next()) {
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
