package com.markus.app.dto;

import java.util.List;

import com.markus.app.model.PagingBO;

public class ResultsDTO {
	private Integer page;
	private Integer size;
	private Integer totalPages;
	private Long totalItems;
	private List<? extends AbstractDTO> data;
	
	public ResultsDTO(List<? extends AbstractDTO> data, PagingBO paging) {
		this.data = data;
		if (paging != null){
			if (paging.getNumberOfPages() == null || paging.getNumberOfPages().equals(Integer.valueOf(0))){
				paging.setNumberOfPages(1);
			}
			this.page = paging.getPageNumber();
			this.size = paging.getPageSize();
			this.totalPages = paging.getNumberOfPages();
			this.totalItems = paging.getTotalItems();
		}
	}

	public Integer getPage() {
		return page;
	}

	public Integer getSize() {
		return size;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public Long getTotalItems() {
		return totalItems;
	}

	public List<? extends AbstractDTO> getData() {
		return data;
	}
}
