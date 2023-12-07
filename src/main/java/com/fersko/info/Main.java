package com.fersko.info;

import com.fersko.info.dto.CheckDto;
import com.fersko.info.dto.PeerDto;
import com.fersko.info.dto.TaskDto;
import com.fersko.info.repository.impl.CheckRepositoryImpl;
import com.fersko.info.service.impl.CheckServiceImpl;

import java.time.LocalDate;

public class Main {


    public static void main(String[] args) {
        CheckServiceImpl checkServiceImpl = new CheckServiceImpl(CheckRepositoryImpl.getChecksRepository());
        checkServiceImpl.update(
                new CheckDto(
                        8L,
                        new PeerDto("poison", LocalDate.now()),
                        new TaskDto("CPP2_s21_containers", null, 500),
                        LocalDate.now()
                )
        );
    }
}
