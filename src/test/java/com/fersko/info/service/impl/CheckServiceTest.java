package com.fersko.info.service.impl;

import com.fersko.info.mapper.CheckMapper;
import com.fersko.info.repository.CheckRepository;
import com.fersko.info.service.CheckService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CheckServiceTest {

    @Mock
    private CheckRepository checkRepository;

    @Mock
    private CheckMapper checkMapper;

    @InjectMocks
    private CheckService checkService;


    public CheckServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void  findByIdTest() {

    }

}