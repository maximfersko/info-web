package com.fersko.info.service;

import com.fersko.info.entity.Task;
import com.fersko.info.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class TaskService implements BaseService<Task, String> {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Optional<Task> findById(String id) {
        return taskRepository.findById(id);
    }

    @Override
    public void update(Task entity) {
        taskRepository.update(entity);
    }

    @Override
    public boolean delete(String id) {
        return taskRepository.delete(id);
    }

    @Override
    public Task save(Task entity) {
        return taskRepository.save(entity);
    }

    @Override
    public List<Task> findByAll() {
        return taskRepository.findByAll();
    }
}
