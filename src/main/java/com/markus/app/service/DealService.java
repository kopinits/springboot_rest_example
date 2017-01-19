package com.markus.app.service;

import com.markus.app.dto.DealDTO;
import com.markus.app.model.filter.DealFilter;
import com.markus.app.support.PageableFindAll;


public interface DealService extends PageableFindAll<DealDTO, DealFilter>{
	
	public DealDTO findById(Long id);
	public DealDTO insert(DealDTO deal);
	public DealDTO update(DealDTO deal);
}
