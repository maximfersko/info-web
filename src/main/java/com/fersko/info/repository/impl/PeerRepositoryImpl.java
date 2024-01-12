package com.fersko.info.repository.impl;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Peer;
import com.fersko.info.repository.PeerRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PeerRepositoryImpl implements PeerRepository {
	private static final String DELETE_SQL =
			"DELETE FROM peers WHERE id = ?";
	private static final String SAVE_SQL =
			"INSERT INTO peers (pk_nickname, birthday) VALUES (?, ?)";
	private static final String FIND_ALL_SQL =
			"SELECT id, pk_nickname, birthday FROM peers";
	private static final String FIND_BY_ID =
			FIND_ALL_SQL + " WHERE id = ?";
	private static final String UPDATE_SQL =
			"UPDATE peers SET pk_nickname = ?, birthday = ? WHERE id = ?";
	private final ConnectionManager connectionManager;

	public PeerRepositoryImpl() {
		connectionManager = new ConnectionManager();
	}

	public PeerRepositoryImpl(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	@Override
	public Optional<Peer> findById(Long id) {
		try (Connection connection = connectionManager.getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
			preparedStatement.setLong(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return Optional.of(getPeer(resultSet));
			}
			return Optional.empty();
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return Optional.empty();
	}


	@Override
	public Peer update(Peer entity) {
		try (Connection connection = connectionManager.getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
			preparedStatement.setString(1, entity.getPkNickname());
			preparedStatement.setDate(2, Date.valueOf(entity.getBirthday()));
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
	public Peer save(Peer entity) {
		try (Connection connection = connectionManager.getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, entity.getPkNickname());
			preparedStatement.setDate(2, Date.valueOf(entity.getBirthday()));
			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
					if (resultSet.next()) {
						entity.setId(resultSet.getLong(1));
					}
				}
			} else {
				log.error("Saving peer failed, no rows affected.");
			}

			return entity;
		} catch (SQLException e) {
			log.error("Error saving peer: {}", e.getMessage(), e);
		}

		return null;
	}


	@Override
	public List<Peer> findByAll() {
		List<Peer> peers = null;
		try (Connection connection = connectionManager.getConnection();
		     Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
			if (resultSet.next()) {
				peers = new ArrayList<>();
				while (resultSet.next()) {
					peers.add(getPeer(resultSet));
				}
			}
			return peers;
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		return peers;
	}

	private Peer getPeer(ResultSet resultSet) throws SQLException {
		return new Peer(
				resultSet.getLong("id"),
				resultSet.getString("pk_nickname"),
				resultSet.getDate("birthday").toLocalDate()
		);
	}
}
