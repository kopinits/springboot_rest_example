package com.markus.app.model.filter;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FilterCriteria<T> {
	private T value;
	private QueryOption queryOption;
	private boolean isSet = false;

	public FilterCriteria() {
		this.isSet = false;
	}

	public FilterCriteria(T value) {
		this(value, null, false);
	}

	public FilterCriteria(T value, QueryOption option) {
		this(value, option, false);
	}

	public FilterCriteria(T value, QueryOption option, boolean nullValueAllowed) {
		this.value = value;
		this.isSet = this.value != null || (nullValueAllowed && option != null);
		if (this.isSet) {
			this.queryOption = option != null ? option : QueryOption.EQ;
		}
	}

	public T getValue() {
		return value;
	}

	public QueryOption getQueryOption() {
		return queryOption;
	}

	public boolean isSet() {
		return isSet;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
