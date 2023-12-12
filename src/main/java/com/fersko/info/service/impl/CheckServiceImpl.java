package com.fersko.info.service.impl;

import com.fersko.info.dto.CheckDto;
import com.fersko.info.mapper.CheckMapper;
import com.fersko.info.repository.impl.CheckRepositoryImpl;
import com.fersko.info.service.CheckService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CheckServiceImpl implements CheckService {

    private final CheckMapper checkMapper;
    private final CheckRepositoryImpl checkRepositoryImpl;

    public CheckServiceImpl() {
        checkMapper = new CheckMapper();
        checkRepositoryImpl = new CheckRepositoryImpl();
    }


    @Override
    public Optional<CheckDto> findById(Long id) {
        return checkRepositoryImpl.findById(id).map(checkMapper::toDto);
    }

    @Override
    public CheckDto update(CheckDto entity) {
        return checkMapper.toDto(checkRepositoryImpl.update(checkMapper.fromDto(entity)));
    }

    @Override
    public boolean delete(Long id) {
        return checkRepositoryImpl.delete(id);
    }

    @Override
    public CheckDto save(CheckDto entity) {
        return checkMapper.toDto(checkRepositoryImpl.save(checkMapper.fromDto(entity)));
    }

    @Override
    public List<CheckDto> findByAll() {
        return checkRepositoryImpl.findByAll()
                .stream()
                .map(checkMapper::toDto)
                .toList();
    }
}
