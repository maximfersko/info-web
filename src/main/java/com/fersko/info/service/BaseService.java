package com.fersko.info.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, I> {


    Optional<T> findById(I id);

    void update(T entity);

    boolean delete(I id);

    T save(T entity);

    List<T> findByAll();
}
