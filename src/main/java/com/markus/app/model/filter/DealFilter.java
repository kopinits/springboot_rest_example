package com.markus.app.model.filter;

import org.joda.time.DateTime;

import com.markus.app.model.Deal;

public class DealFilter implements Filter<Deal>{

	private FilterCriteria<String> name = new FilterCriteria<String>();
	private FilterCriteria<DateTime> startDate = new FilterCriteria<DateTime>();
	public FilterCriteria<String> getName() {
		return name;
	}
	public void setName(String name) {
		this.name = new FilterCriteria<String>(name, QueryOption.LIKE);
	}
	public FilterCriteria<DateTime> getStartDate() {
		return startDate;
	}
	public void setStartDate(DateTime startDate, QueryOption option) {
		this.startDate = new FilterCriteria<DateTime>(startDate, option);
	}
}
