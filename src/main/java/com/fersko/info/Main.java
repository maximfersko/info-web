package com.fersko.info;



import com.fersko.info.entity.Check;
import com.fersko.info.entity.Peer;
import com.fersko.info.entity.Task;
import com.fersko.info.repository.ChecksRepository;
import com.fersko.info.repository.PeerRepository;
import com.fersko.info.repository.TaskRepository;
import com.fersko.info.service.CheckService;
import com.fersko.info.service.PeerService;
import com.fersko.info.service.TaskService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws SQLException {
        CheckService service = new CheckService(ChecksRepository.getChecksRepository());
        PeerService peerService = new PeerService(PeerRepository.getPeerRepository());
        TaskService taskService = new TaskService(TaskRepository.getTaskRepository());
        System.out.println(taskService.findById("DO1_Linux"));
//        System.out.println(service.findByAll());


    }
}
