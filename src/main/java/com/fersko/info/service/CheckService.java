package com.fersko.info.service;

import com.fersko.info.entity.Check;
import com.fersko.info.repository.ChecksRepository;

import java.util.List;
import java.util.Optional;

public class CheckService implements BaseService<Check, Integer>{

    private final ChecksRepository checksRepository;

    public CheckService(ChecksRepository checksRepository) {
        this.checksRepository = checksRepository;
    }


    @Override
    public Optional<Check> findById(Integer id) {
        return checksRepository.findById(id);
    }

    @Override
    public void update(Check entity) {
        checksRepository.update(entity);
    }

    @Override
    public boolean delete(Integer id) {
        return checksRepository.delete(id);
    }

    @Override
    public Check save(Check entity) {
        return checksRepository.save(entity);
    }

    @Override
    public List<Check> findByAll() {
        return checksRepository.findByAll();
    }
}
