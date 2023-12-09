package com.fersko.info;

import com.fersko.info.dto.CheckDto;
import com.fersko.info.dto.PeerDto;
import com.fersko.info.dto.TaskDto;
import com.fersko.info.service.impl.CheckServiceImpl;

import java.time.LocalDate;

public class Main {


    public static void main(String[] args) {
        CheckServiceImpl checkServiceImpl = new CheckServiceImpl();
        checkServiceImpl.save(
                new CheckDto(
                        17L,
                        new PeerDto("poison", LocalDate.now()),
                        new TaskDto("CPP2_s21_containers", null, 500),
                        LocalDate.now()
                )
        );
    }
}
