package com.markus.app.rest.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.markus.app.dto.DealDTO;
import com.markus.app.model.filter.DealFilter;
import com.markus.app.service.DealService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DealServiceTest {

	private static final String UPDATED_DEAL_NAME = "Updated Deal Name";
	private static final Date DEFAULT_START_DATE = new Date(1473458400000L);
	private static final String DEFAULT_DEAL_NAME_01 = "Deal Name 01";

	@Autowired
	private DealService service;

	@Test
	@Sql("/testdata/deal/deal-test.sql")
	public void getDealTest() {
		DealDTO defaultDeal = createDefaultDeal();
		DealDTO findById = service.findById(Long.valueOf(1));
		assertThat(findById.getId()).isNotNull();
		assertThat(findById.getId()).isEqualTo(1l);
		assertThat(findById.getName()).isEqualTo(defaultDeal.getName());
		assertThat(findById.getStartDate()).isEqualTo(defaultDeal.getStartDate());
	}

	@Test
	@Sql("/testdata/deal/deal-test.sql")
	public void getAllDealTest() {
		List<DealDTO> findAll = service.findAll(new DealFilter());
		assertThat(findAll).isNotNull();
		assertThat(findAll.size()).isEqualTo(5);
	}

	@Test
	@Sql("/testdata/deal/deal-test.sql")
	public void createDealTest() {
		DealDTO inserted = service.insert(createDefaultDeal());
		assertThat(inserted.getId()).isNotNull();
		assertThat(inserted.getName()).isEqualTo(DEFAULT_DEAL_NAME_01);
		assertThat(inserted.getStartDate()).isEqualTo(DEFAULT_START_DATE);
	}

	@Test
	@Sql("/testdata/deal/deal-test.sql")
	public void updateDealTest() {
		DealDTO persisted = service.findById(Long.valueOf(1));
		persisted.setName(UPDATED_DEAL_NAME);
		Date updatedStartDate = new Date();
		persisted.setStartDate(updatedStartDate);
		service.update(persisted);
		assertThat(persisted.getId()).isEqualTo(1L);
		assertThat(persisted.getName()).isNotEqualTo(DEFAULT_DEAL_NAME_01);
		assertThat(persisted.getName()).isEqualTo(UPDATED_DEAL_NAME);
		assertThat(persisted.getStartDate()).isNotEqualTo(DEFAULT_START_DATE);
		assertThat(persisted.getStartDate()).isEqualTo(updatedStartDate);
	}

	@Test
	@Sql("/testdata/deal/deal-test.sql")
	public void optimisticLockDealTest() {
		DealDTO persisted = service.findById(Long.valueOf(1));
		persisted.setName(UPDATED_DEAL_NAME);
		Date updatedStartDate = new Date();
		persisted.setStartDate(updatedStartDate);
		persisted.setVersion(1);
		service.update(persisted);
		assertThat(false);
	}

	private DealDTO createDefaultDeal() {
		DealDTO deal = new DealDTO();
		deal.setName(DEFAULT_DEAL_NAME_01);
		deal.setStartDate(DEFAULT_START_DATE);
		return deal;
	}
}