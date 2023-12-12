package com.fersko.info.service.impl;

import com.fersko.info.dto.PeerDto;
import com.fersko.info.entity.Peer;
import com.fersko.info.mapper.PeerMapper;
import com.fersko.info.repository.impl.PeerRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PeerServiceImplTest {

    @Mock
    private PeerRepositoryImpl peerRepositoryMock;

    @Mock
    private PeerMapper peerMapperMock;

    @InjectMocks
    private PeerServiceImpl peerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_shouldReturnOptionalPeerDto_whenIdExists() {
        Long peerId = 1L;
        PeerDto expectedPeerDto = createSamplePeerDto();

        when(peerRepositoryMock.findById(peerId)).thenReturn(Optional.of(createSamplePeer()));
        when(peerMapperMock.toDto(any())).thenReturn(expectedPeerDto);

        Optional<PeerDto> result = peerService.findById(peerId);

        assertTrue(result.isPresent());
        assertEquals(expectedPeerDto, result.get());

        verify(peerRepositoryMock, times(1)).findById(peerId);
        verify(peerMapperMock, times(1)).toDto(any());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {
        Long nonExistentPeerId = 100L;

        when(peerRepositoryMock.findById(nonExistentPeerId)).thenReturn(Optional.empty());

        Optional<PeerDto> result = peerService.findById(nonExistentPeerId);

        assertTrue(result.isEmpty());

        verify(peerRepositoryMock, times(1)).findById(nonExistentPeerId);
        verifyNoInteractions(peerMapperMock);
    }

    @Test
    void update_shouldInvokeUpdateMethodInRepository() {
        PeerDto peerDtoToUpdate = createSamplePeerDto();

        peerService.update(peerDtoToUpdate);

        verify(peerRepositoryMock, times(1)).update(any());
    }

    @Test
    void delete_shouldReturnTrue_whenPeerIsDeleted() {
        Long peerIdToDelete = 1L;

        when(peerRepositoryMock.delete(peerIdToDelete)).thenReturn(true);

        boolean result = peerService.delete(peerIdToDelete);

        assertTrue(result);

        verify(peerRepositoryMock, times(1)).delete(peerIdToDelete);
    }

    @Test
    void delete_shouldReturnFalse_whenPeerIsNotDeleted() {
        Long peerIdToDelete = 1L;

        when(peerRepositoryMock.delete(peerIdToDelete)).thenReturn(false);

        boolean result = peerService.delete(peerIdToDelete);

        assertFalse(result);

        verify(peerRepositoryMock, times(1)).delete(peerIdToDelete);
    }

    @Test
    void save_shouldReturnSavedPeerDto() {
        PeerDto peerDtoToSave = createSamplePeerDto();

        when(peerRepositoryMock.save(any())).thenReturn(createSamplePeer());
        when(peerMapperMock.toDto(any())).thenReturn(peerDtoToSave);

        PeerDto savedPeerDto = peerService.save(peerDtoToSave);

        assertNotNull(savedPeerDto);
        assertEquals(peerDtoToSave, savedPeerDto);

        verify(peerRepositoryMock, times(1)).save(any());
        verify(peerMapperMock, times(1)).toDto(any());
    }

    private Peer createSamplePeer() {
        return new Peer(1L, "peer", LocalDate.now());
    }

    private PeerDto createSamplePeerDto() {
        return new PeerDto(1L, "peer", LocalDate.now());
    }
}
