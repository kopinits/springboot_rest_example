package com.markus.app.model;

public class PagingBO {
	private Long totalItems;
	private Integer pageSize;
	private Integer pageNumber;
	private Integer numberOfPages;
	private SortDirection sortDirection;
	private String orderBy;

	public PagingBO(Integer page, Integer size, SortDirection sortDirection, String orderBy) {
		this.pageNumber = page != null ? page : Integer.valueOf(0);
		this.pageSize = size != null ? size : Integer.valueOf(10);
		this.sortDirection = sortDirection;
		this.orderBy = orderBy;
	}

	public Long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Long totalItems) {
		this.totalItems = totalItems;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public SortDirection getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(SortDirection sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

}
