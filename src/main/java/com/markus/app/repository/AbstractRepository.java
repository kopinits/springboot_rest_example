package com.markus.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.markus.app.model.AbstractEntity;

@Repository
public interface AbstractRepository <T extends AbstractEntity>{
	public T findById(final Long id);
	public T save(T obj);
	public Page<T> findAll(Specification<T> filter, Pageable pageable);
	public T saveAndFlush(T obj);

}
