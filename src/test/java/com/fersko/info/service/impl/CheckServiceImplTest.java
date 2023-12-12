package com.fersko.info.service.impl;

import com.fersko.info.dto.CheckDto;
import com.fersko.info.dto.PeerDto;
import com.fersko.info.dto.TaskDto;
import com.fersko.info.mapper.CheckMapper;
import com.fersko.info.repository.impl.CheckRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CheckServiceImplTest {

    @Mock
    private CheckMapper checkMapper;

    @Mock
    private CheckRepositoryImpl checkRepositoryImpl;

    @InjectMocks
    private CheckServiceImpl checkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {
        Long checkId = 1L;
        when(checkRepositoryImpl.findById(checkId)).thenReturn(Optional.empty());

        Optional<CheckDto> result = checkService.findById(checkId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdate() {
        CheckDto checkDto = createSampleCheckDto();
        checkService.update(checkDto);

        verify(checkRepositoryImpl).update(checkMapper.fromDto(checkDto));
    }

    @Test
    void delete_shouldReturnTrue_whenCheckIsDeleted() {
        Long checkId = 1L;
        when(checkRepositoryImpl.delete(checkId)).thenReturn(true);

        boolean result = checkService.delete(checkId);

        assertTrue(result);
    }

    @Test
    void delete_shouldReturnFalse_whenCheckIsNotDeleted() {
        Long checkId = 1L;
        when(checkRepositoryImpl.delete(checkId)).thenReturn(false);

        boolean result = checkService.delete(checkId);

        assertFalse(result);
    }


    private CheckDto createSampleCheckDto() {
        LocalDate date = LocalDate.now();
        return new CheckDto(
                1L,
                new PeerDto(1L, "nickname", LocalDate.of(2000, 1, 1)),
                new TaskDto(1L, "taskTitle", null, 100),
                date
        );
    }

}