package com.markus.app.controller;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.markus.app.dto.DealDTO;
import com.markus.app.dto.ResultsDTO;
import com.markus.app.exception.BusinessException;
import com.markus.app.model.PagingBO;
import com.markus.app.model.filter.DealFilter;
import com.markus.app.service.DealService;

@RestController
@RequestMapping("/deal")
public class DealController extends AbstractController<DealFilter> {
	
	@Autowired
	private DealService service;
	protected DealController() {
		//@formatter:off
		super(DealFilter.class,
			new QueryParam("name", String.class),
			new QueryParam("startDate", DateTime.class).withOption());
		//@formatter:on
	}

	@ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public DealDTO getById(@PathVariable("id") Long id) {
    	return service.findById(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResultsDTO listAll(@RequestParam final Map<String, String> params) {
    	PagingBO paging = createPagingConfiguration(params);
    	DealFilter filter = createFilter(params);
        List<DealDTO> findAll = service.findAll(filter, paging);
        return new ResultsDTO(findAll, paging);
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public DealDTO save(@RequestBody DealDTO deal) throws BusinessException {
    	return service.insert(deal);
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public DealDTO update(@RequestBody DealDTO deal) throws BusinessException {
    	return service.update(deal);
    }
}
