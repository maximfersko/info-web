package com.fersko.info.service;

import com.fersko.info.dto.BaseDto;

import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseDto> {


    Optional<T> findById(Long id);

    T update(T entity);

    boolean delete(Long id);

    T save(T entity);

    List<T> findByAll();
}
