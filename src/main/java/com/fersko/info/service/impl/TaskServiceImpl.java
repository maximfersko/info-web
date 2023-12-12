package com.fersko.info.service.impl;

import com.fersko.info.dto.TaskDto;
import com.fersko.info.mapper.TaskMapper;
import com.fersko.info.repository.impl.TaskRepositoryImpl;
import com.fersko.info.service.TaskService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepositoryImpl taskRepositoryImpl;

    private final TaskMapper taskMapper;

    public TaskServiceImpl() {
        taskRepositoryImpl = new TaskRepositoryImpl();
        taskMapper = new TaskMapper();
    }


    @Override
    public Optional<TaskDto> findById(Long id) {
        return taskRepositoryImpl.findById(id).map(taskMapper::toDto);
    }

    @Override
    public TaskDto update(TaskDto entity) {
        return taskMapper.toDto(taskRepositoryImpl.update(taskMapper.fromDto(entity)));
    }

    @Override
    public boolean delete(Long id) {
        return taskRepositoryImpl.delete(id);
    }

    @Override
    public TaskDto save(TaskDto entity) {
        return taskMapper.toDto(taskRepositoryImpl.save(taskMapper.fromDto(entity)));
    }

    @Override
    public List<TaskDto> findByAll() {
        return taskRepositoryImpl.findByAll()
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
