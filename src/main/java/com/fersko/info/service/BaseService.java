package com.fersko.info.service;

import com.fersko.info.dto.BaseDto;

import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseDto, I> {


    Optional<T> findById(I id);

    void update(T entity);

    boolean delete(I id);

    T save(T entity);

    List<T> findByAll();
}
