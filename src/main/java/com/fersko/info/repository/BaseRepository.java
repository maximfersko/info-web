package com.fersko.info.repository;

import com.fersko.info.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T extends BaseEntity<I>, I> {

    Optional<T> findById(I id);

    void update(T entity);

    boolean delete(I id);

    T save(T entity);

    List<T> findByAll();
}
