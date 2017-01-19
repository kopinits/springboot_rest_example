package com.markus.app.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import com.markus.app.model.AbstractEntity;
import com.markus.app.model.filter.Filter;

public abstract class AbstractSpecification<T extends AbstractEntity, F extends Filter<T>>
		implements ISpecification<T, F> {

	@Override
	public Specification<T> filter(F filter) {
		List<Specification<T>> specifications = new ArrayList<>();
		Specification<T> result = null;

		for (Specification<T> spec : specifications) {
			if (result == null) {
				result = spec;
			} else {
				result = Specifications.where(result).and(spec);
			}
		}
		return result;
	}
	
	protected abstract void buildSpecification(final List<Specification<T>> specifications, final F filter);

}
