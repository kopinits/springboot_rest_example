package com.markus.app.service.impl;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.markus.app.dto.AbstractDTO;
import com.markus.app.model.AbstractEntity;
import com.markus.app.model.PagingBO;
import com.markus.app.model.filter.Filter;
import com.markus.app.repository.AbstractRepository;
import com.markus.app.repository.specification.ISpecification;
import com.markus.app.service.FinderSupport;


@Service 
@Transactional 
public abstract class AbstractServiceImpl <T extends AbstractEntity, F extends Filter<T>, D extends AbstractDTO>{ 
        private static final Logger logger = LoggerFactory.getLogger(AbstractServiceImpl.class); 
        
        @Autowired 
        private FinderSupport finderSupport; 
        
        /** The specification. */ 
        private ISpecification<T, F> specification; 

        /** The repository. */ 
        private AbstractRepository<T> repository; 

        public AbstractServiceImpl(final ISpecification<T, F> specification, 
                        final AbstractRepository<T> repository) { 
                this.specification = specification; 
                this.repository = repository; 
        }         
        
        public List<T> findAllPaged(final F filter, PagingBO paging) { 
                Pageable pageable = finderSupport.createPageRequest(paging); 
                Page<T> page = repository.findAll(specification.filter(filter), pageable); 
                if (paging != null) { 
                        logger.info("paging null. Creating new paging object"); 
                        paging.setNumberOfPages(page.getTotalPages()); 
                        paging.setTotalItems(page.getTotalElements()); 
                } 
                return page.getContent(); 
        } 

        public AbstractRepository<T> getRepository() { 
                return repository; 
        } 
        
} 
