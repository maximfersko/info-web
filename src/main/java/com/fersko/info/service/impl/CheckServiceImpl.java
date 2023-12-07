package com.fersko.info.service.impl;

import com.fersko.info.dto.CheckDto;
import com.fersko.info.mapper.CheckMapper;
import com.fersko.info.repository.impl.CheckRepositoryImpl;
import com.fersko.info.service.CheckService;

import java.util.List;
import java.util.Optional;

public class CheckServiceImpl implements CheckService {

    private final CheckRepositoryImpl checkRepositoryImpl;
    private static final CheckMapper checkMapper = new CheckMapper();

    public CheckServiceImpl(CheckRepositoryImpl checkRepositoryImpl) {
        this.checkRepositoryImpl = checkRepositoryImpl;
    }


    @Override
    public Optional<CheckDto> findById(Long id) {
        return checkRepositoryImpl.findById(id).map(checkMapper::toDto);
    }

    @Override
    public void update(CheckDto entity) {
        checkRepositoryImpl.update(checkMapper.toEntity(entity));
    }

    @Override
    public boolean delete(Long id) {
        return checkRepositoryImpl.delete(id);
    }

    @Override
    public CheckDto save(CheckDto entity) {
        return checkMapper.toDto(checkRepositoryImpl.save(checkMapper.toEntity(entity)));
    }

    @Override
    public List<CheckDto> findByAll() {
        return checkRepositoryImpl.findByAll()
                .stream()
                .map(checkMapper::toDto)
                .toList();
    }
}
