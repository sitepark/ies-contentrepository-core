package com.sitepark.ies.contentrepository.core.domain.entity.query;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class QueryTest {

	@Test
	void testWithNullFilter() {
		Query query = Query.builder()
				.filterBy(null)
				.build();
		assertTrue(query.getFilterBy().isEmpty(), "filter optional should be empty");
	}

}
