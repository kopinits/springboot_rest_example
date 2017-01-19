package com.markus.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.markus.app.model.Deal;

@Repository
public interface DealRepository extends AbstractRepository<Deal>, JpaRepository<Deal, Long>, JpaSpecificationExecutor<Deal>{

}
