package com.fersko.info.repository;

import com.fersko.info.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T extends BaseEntity> {
	Optional<T> findById(Long id);

	T update(T entity);

	boolean delete(Long id);

	T save(T entity);

	List<T> findByAll();
}
