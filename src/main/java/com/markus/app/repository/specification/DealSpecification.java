package com.markus.app.repository.specification;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.markus.app.model.Deal;
import com.markus.app.model.filter.DealFilter;

public class DealSpecification extends AbstractSpecification<Deal, DealFilter>{

	@Override
	protected void buildSpecification(List<Specification<Deal>> specs, DealFilter filter) {
		SpecificationsSupport.addSpecification(specs, Deal.class, String.class, filter.getName(),  "name");
		SpecificationsSupport.addSpecification(specs, Deal.class, DateTime.class, filter.getStartDate(),  "startDate");
	}

}
