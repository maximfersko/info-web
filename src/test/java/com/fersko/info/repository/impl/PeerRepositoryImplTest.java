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
import static org.mockito.Mockito.verify;
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
        String peerId = "testPeer";
        Peer expectedPeer = createSamplePeer();
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("pk_nickname")).thenReturn(peerId);
        when(resultSet.getDate("birthday")).thenReturn(Date.valueOf(expectedPeer.getBirthday()));

        Optional<Peer> result = peerRepository.findById(peerId);

        assertTrue(result.isPresent());
        assertEquals(expectedPeer, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() throws SQLException {
        String peerId = "nonexistentPeer";
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Peer> result = peerRepository.findById(peerId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdate() throws SQLException {
        Peer peer = createSamplePeer();
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        peerRepository.update(peer);

        verify(preparedStatement).setDate(eq(1), any(Date.class));
        verify(preparedStatement).setString(eq(2), anyString());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void delete_shouldReturnTrue_whenPeerIsDeleted() throws SQLException {
        String peerId = "testPeer";
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = peerRepository.delete(peerId);

        assertTrue(result);
    }

    @Test
    void delete_shouldReturnFalse_whenPeerIsNotDeleted() throws SQLException {
        String peerId = "nonexistentPeer";
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
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Peer savedPeer = peerRepository.save(peerToSave);

        assertNotNull(savedPeer);
        assertEquals(peerToSave, savedPeer);
    }


    private Peer createSamplePeer() {
        return new Peer("testPeer", Date.valueOf("2000-01-01").toLocalDate());
    }
}
