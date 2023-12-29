package com.fersko.info.repository.impl;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Check;
import com.fersko.info.entity.Peer;
import com.fersko.info.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CheckRepositoryImplTest {

	@Mock
	private ConnectionManager connectionManager;

	@Mock
	private Connection connection;

	@Mock
	private PreparedStatement preparedStatement;

	@Mock
	private ResultSet resultSet;

	@InjectMocks
	private CheckRepositoryImpl checkRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void findById_shouldReturnCheck_whenIdExists() throws SQLException {
		Long checkId = 1L;
		Check expectedCheck = createSampleCheck();
		when(connectionManager.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getLong("id")).thenReturn(checkId);
		when(resultSet.getString("pk_nickname")).thenReturn(expectedCheck.getPeer().getPkNickname());
		when(resultSet.getDate("birthday")).thenReturn(Date.valueOf(expectedCheck.getPeer().getBirthday()));
		when(resultSet.getString("pk_title")).thenReturn(expectedCheck.getTask().getPkTitle());
		when(resultSet.getInt("max_xp")).thenReturn(expectedCheck.getTask().getMaxXp());
		when(resultSet.getDate("date")).thenReturn(Date.valueOf(expectedCheck.getDate()));

		Optional<Check> result = checkRepository.findById(checkId);

		assertTrue(result.isPresent());
		assertEquals(expectedCheck, result.get());
	}


	@Test
	void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() throws SQLException {
		Long checkId = 1L;
		when(connectionManager.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(false);

		Optional<Check> result = checkRepository.findById(checkId);

		assertTrue(result.isEmpty());
	}

	@Test
	void testUpdate() throws SQLException {
		Check check = createSampleCheck();
		when(connectionManager.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

		checkRepository.update(check);

		verify(preparedStatement).setString(eq(1), anyString());
		verify(preparedStatement).setString(eq(2), anyString());
		verify(preparedStatement).setTimestamp(eq(3), any(Timestamp.class));
		verify(preparedStatement).setLong(eq(4), anyLong());
		verify(preparedStatement).executeUpdate();
	}

	@Test
	void delete_shouldReturnTrue_whenCheckIsDeleted() throws SQLException {
		Long checkId = 1L;
		when(connectionManager.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
		when(preparedStatement.executeUpdate()).thenReturn(1);

		boolean result = checkRepository.delete(checkId);

		assertTrue(result);
	}

	@Test
	void delete_shouldReturnFalse_whenCheckIsNotDeleted() throws SQLException {
		Long checkId = 1L;
		when(connectionManager.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
		when(preparedStatement.executeUpdate()).thenReturn(0);

		boolean result = checkRepository.delete(checkId);

		assertFalse(result);
	}

	private Check createSampleCheck() {
		Peer peer = new Peer(0L, "nickname", LocalDate.parse("2000-01-01"));
		Task task = new Task(0L, "hh", null, 100);
		return new Check(1L, peer, task, LocalDate.parse("2022-01-01"));
	}

}
