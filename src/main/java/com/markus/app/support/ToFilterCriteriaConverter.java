package com.markus.app.support;

import com.markus.app.model.filter.FilterCriteria;
import com.markus.app.model.filter.QueryOption;

public interface ToFilterCriteriaConverter<A, B> {
	FilterCriteria<B> convertToFilterCriteria(A value, QueryOption option);
}
