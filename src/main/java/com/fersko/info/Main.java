package com.fersko.info;



import com.fersko.info.repository.ChecksRepository;
import com.fersko.info.service.CheckService;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        CheckService service = new CheckService(ChecksRepository.getChecksRepository());
        System.out.println(service.findById(6));

    }
}
