package com.markus.app.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.markus.app.model.AbstractEntity;
import com.markus.app.model.filter.Filter;

public interface ISpecification<T extends AbstractEntity, F extends Filter<T>> {
	Specification<T> filter(F filter);
}
