package com.fersko.info.repository.impl;

import com.fersko.info.config.ConnectionManager;
import com.fersko.info.entity.Friend;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FriendRepositoryImplTest {

    @Mock
    private ConnectionManager connectionManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private FriendRepositoryImpl friendRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_shouldReturnFriend_whenIdExists() throws SQLException {
        Long friendId = 1L;
        Friend expectedFriend = createSampleFriend();
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(friendId);
        when(resultSet.getString("peer1_nickname")).thenReturn(expectedFriend.getFirstPeer().getId());
        when(resultSet.getDate("peer1_birthday")).thenReturn(Date.valueOf(expectedFriend.getFirstPeer().getBirthday()));
        when(resultSet.getString("peer2_nickname")).thenReturn(expectedFriend.getSecondPeer().getId());
        when(resultSet.getDate("peer2_birthday")).thenReturn(Date.valueOf(expectedFriend.getSecondPeer().getBirthday()));

        Optional<Friend> result = friendRepository.findById(friendId);

        assertTrue(result.isPresent());
        assertEquals(expectedFriend, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() throws SQLException {
        Long friendId = 1L;
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Friend> result = friendRepository.findById(friendId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdate() throws SQLException {
        Friend friend = createSampleFriend();
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        friendRepository.update(friend);

        verify(preparedStatement).setString(eq(1), anyString());
        verify(preparedStatement).setString(eq(2), anyString());
        verify(preparedStatement).setLong(eq(3), anyLong());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void delete_shouldReturnTrue_whenFriendIsDeleted() throws SQLException {
        Long friendId = 1L;
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = friendRepository.delete(friendId);

        assertTrue(result);
    }

    @Test
    void delete_shouldReturnFalse_whenFriendIsNotDeleted() throws SQLException {
        Long friendId = 1L;
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = friendRepository.delete(friendId);

        assertFalse(result);
    }


    private Friend createSampleFriend() {
        Peer firstPeer = new Peer("nickname1", Date.valueOf("2000-01-01").toLocalDate());
        Peer secondPeer = new Peer("nickname2", Date.valueOf("1995-12-31").toLocalDate());
        return new Friend(1L, firstPeer, secondPeer);
    }
}
