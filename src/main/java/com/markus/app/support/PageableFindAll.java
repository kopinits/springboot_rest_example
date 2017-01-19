package com.markus.app.support;

import java.util.List;

import com.markus.app.model.PagingBO;
import com.markus.app.model.filter.Filter;

public interface PageableFindAll<T, F extends Filter<?>> {
	public List<T> findAll(F Filter, PagingBO paging);

	public List<T> findAll(F Filter);
}
