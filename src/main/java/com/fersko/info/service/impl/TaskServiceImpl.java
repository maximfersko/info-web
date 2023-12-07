package com.fersko.info.service.impl;

import com.fersko.info.dto.TaskDto;
import com.fersko.info.entity.Task;
import com.fersko.info.mapper.TaskMapper;
import com.fersko.info.repository.impl.TaskRepositoryImpl;
import com.fersko.info.service.BaseService;
import com.fersko.info.service.TaskService;

import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {

    private final TaskRepositoryImpl taskRepositoryImpl;

    private final TaskMapper taskMapper = new TaskMapper();

    public TaskServiceImpl(TaskRepositoryImpl taskRepositoryImpl) {
        this.taskRepositoryImpl = taskRepositoryImpl;
    }

    @Override
    public Optional<TaskDto> findById(String id) {
        return taskRepositoryImpl.findById(id).map(taskMapper::toDto);
    }

    @Override
    public void update(TaskDto entity) {
        taskRepositoryImpl.update(taskMapper.toEntity(entity));
    }

    @Override
    public boolean delete(String id) {
        return taskRepositoryImpl.delete(id);
    }

    @Override
    public TaskDto save(TaskDto entity) {
        return taskMapper.toDto(taskRepositoryImpl.save(taskMapper.toEntity(entity)));
    }

    @Override
    public List<TaskDto> findByAll() {
        return taskRepositoryImpl.findByAll()
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
