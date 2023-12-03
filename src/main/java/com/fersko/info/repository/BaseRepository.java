package com.fersko.info.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, I> {

    Optional<T> findById(I id);

    void update(T entity);

    boolean delete(I id);

    T save(T entity);

    List<T> findByAll();
}
