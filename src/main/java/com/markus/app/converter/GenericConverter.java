package com.markus.app.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GenericConverter {
	
	private ModelMapper mapper = new ModelMapper();
	
	public <T,U> T fromDTO(U dto, Class<T> targetClass){
		return mapper.map(dto, targetClass);
	}

	public <T,U> U toDTO(T entity, Class<U> targetClass){
		return mapper.map(entity, targetClass);
	}

	public <T,U> List<U> toDTO(List<T> list, Class<U> targetClass){
		List<U> results = new ArrayList<U>();
		for (T entity : list){
			results.add(toDTO(entity, targetClass));
		}
		return results;
	}
}
