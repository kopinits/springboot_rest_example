package com.markus.app.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.markus.app.converter.GenericConverter;
import com.markus.app.dto.DealDTO;
import com.markus.app.model.Deal;
import com.markus.app.model.PagingBO;
import com.markus.app.model.filter.DealFilter;
import com.markus.app.repository.DealRepository;
import com.markus.app.repository.specification.DealSpecification;
import com.markus.app.service.DealService;

@Service
@Qualifier("DealService")
@Transactional
public class DealServiceImpl extends AbstractServiceImpl<Deal, DealFilter, DealDTO> implements DealService {

	@Autowired
	private GenericConverter converter;
	
	@Autowired
	public DealServiceImpl(DealRepository repository, DealSpecification specification) {
		super(specification, repository);
	}

	@Override
	public DealDTO findById(Long id) {
		return converter.toDTO(getRepository().findById(id), DealDTO.class);
	}

	@Override
	public DealDTO insert(DealDTO dealDTO) {
		Deal deal = converter.fromDTO(dealDTO, Deal.class);
		deal.setCreationTime(new Date());
		return converter.toDTO(getRepository().saveAndFlush(deal), DealDTO.class);
	}

	@Override
	public DealDTO update(DealDTO dealDTO) {
		Deal deal = converter.fromDTO(dealDTO, Deal.class);
		deal.setLastUpdateTime(new Date());
		return converter.toDTO(getRepository().saveAndFlush(deal), DealDTO.class);
	}

	@Override
	public List<DealDTO> findAll(DealFilter filter, PagingBO paging) {
		List<Deal> findAllPaged = super.findAllPaged(filter, paging);
		return converter.toDTO(findAllPaged, DealDTO.class);
	}

	@Override
	public List<DealDTO> findAll(DealFilter filter) {
		return findAll(filter, null);
	}
}
