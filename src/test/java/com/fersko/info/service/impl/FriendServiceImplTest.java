package com.fersko.info.service.impl;

import com.fersko.info.dto.FriendDto;
import com.fersko.info.entity.Friend;
import com.fersko.info.entity.Peer;
import com.fersko.info.mapper.FriendMapper;
import com.fersko.info.repository.impl.FriendRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class FriendServiceImplTest {

    @Mock
    private FriendRepositoryImpl friendRepository;

    @Mock
    private FriendMapper friendMapper;

    @InjectMocks
    private FriendServiceImpl friendService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {
        Long friendId = 1L;

        when(friendRepository.findById(friendId)).thenReturn(Optional.empty());

        Optional<FriendDto> result = friendService.findById(friendId);

        assertTrue(result.isEmpty());

        verify(friendRepository, times(1)).findById(friendId);
        verify(friendMapper, never()).toDto(any());
    }

    @Test
    void update_shouldCallRepositoryUpdate() {
        FriendDto friendDtoToUpdate = createSampleFriendDto(1L);

        friendService.update(friendDtoToUpdate);

        verify(friendRepository, times(1)).update(any());
    }

    @Test
    void delete_shouldReturnTrue_whenFriendIsDeleted() {
        Long friendId = 1L;

        when(friendRepository.delete(friendId)).thenReturn(true);

        boolean result = friendService.delete(friendId);

        assertTrue(result);

        verify(friendRepository, times(1)).delete(friendId);
    }

    @Test
    void delete_shouldReturnFalse_whenFriendIsNotDeleted() {
        Long friendId = 1L;

        when(friendRepository.delete(friendId)).thenReturn(false);

        boolean result = friendService.delete(friendId);

        assertFalse(result);

        verify(friendRepository, times(1)).delete(friendId);
    }


    private Friend createSampleFriend(Long id) {
        return new Friend(
                id,
                new Peer("peer", LocalDate.parse("2022-01-01")),
                new Peer("peer2", LocalDate.parse("2022-02-01"))
        );
    }

    private FriendDto createSampleFriendDto(Long id) {
        return friendMapper.toDto(createSampleFriend(id));
    }
}
