package com.fersko.info.repository.impl;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Peer;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class PeerRepositoryImplTest {

    @Mock
    private ConnectionManager connectionManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private PeerRepositoryImpl peerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_shouldReturnPeer_whenIdExists() throws SQLException {
        Long peerId = 1L;
        Peer expectedPeer = createSamplePeer();
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(peerId);
        when(resultSet.getDate("birthday")).thenReturn(Date.valueOf(expectedPeer.getBirthday()));

        Optional<Peer> result = peerRepository.findById(peerId);

        assertTrue(result.isPresent());
        assertEquals(expectedPeer, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() throws SQLException {
        Long peerId = 999L;
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Peer> result = peerRepository.findById(peerId);

        assertTrue(result.isEmpty());
    }

    @Test
    void delete_shouldReturnTrue_whenPeerIsDeleted() throws SQLException {
        Long peerId = 1L;
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = peerRepository.delete(peerId);

        assertTrue(result);
    }

    @Test
    void delete_shouldReturnFalse_whenPeerIsNotDeleted() throws SQLException {
        Long peerId = 999L;
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = peerRepository.delete(peerId);

        assertFalse(result);
    }

    @Test
    void save_shouldReturnSavedPeer() throws SQLException {
        Peer peerToSave = createSamplePeer();
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        Peer savedPeer = peerRepository.save(peerToSave);

        assertNotNull(savedPeer);
        assertEquals(1L, savedPeer.getId());
        assertEquals(peerToSave.getPkNickname(), savedPeer.getPkNickname());
        assertEquals(peerToSave.getBirthday(), savedPeer.getBirthday());


    }

    private Peer createSamplePeer() {
        return new Peer(1L, null, Date.valueOf("2000-01-01").toLocalDate());
    }
}
