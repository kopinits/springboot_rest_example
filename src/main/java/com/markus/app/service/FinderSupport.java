package com.markus.app.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.markus.app.model.PagingBO;

@Service
public class FinderSupport {
	/** 
     * Creates a page request for database query. 
     * 
     * @param givenPaging 
     *            the given paging 
     * @return the pageable. 
     */ 
    public Pageable createPageRequest(final PagingBO givenPaging) { 
            PagingBO paging = givenPaging; 
            if (paging == null) { 
                    paging = new PagingBO(null, null, null, null); 
            } 

            // considering ordering criteria 
            String orderByCriteria = paging.getOrderBy(); 
            if (StringUtils.isBlank(orderByCriteria)) { 
                    orderByCriteria = "id"; 
            } 

            // considering direction criteria 
            Sort.Direction directionCriteria = Sort.Direction.ASC; 
            if (paging.getSortDirection() != null) { 
                    switch (paging.getSortDirection()) { 
                    case ASC: 
                            directionCriteria = Sort.Direction.ASC; 
                            break; 
                    case DESC: 
                            directionCriteria = Sort.Direction.DESC; 
                            break; 
                    default: 
                            directionCriteria = Sort.Direction.ASC; 
                            break; 
                    } 
            } 

            // considering page and size criteria 
            Integer pageCriteria = paging.getPageNumber(); 
            Integer sizeCriteria = paging.getPageSize(); 
            if (pageCriteria == null) { 
                    pageCriteria = 0; 
            } 
            if (sizeCriteria == null) { 
                    sizeCriteria = Integer.MAX_VALUE; 
            } 

            return new PageRequest(pageCriteria, sizeCriteria, directionCriteria, orderByCriteria); 
    } 

}
