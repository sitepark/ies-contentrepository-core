package com.sitepark.ies.contentrepository.core.domain.entity.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SubTreeQueryTest {

	@Test
	void testWithNullFilter() {

		SubTreeQuery query = SubTreeQuery.builder()
				.root(123L)
				.filterBy(null)
				.build();

		assertEquals(123L, query.getRootList().get(0), "filter optional should be empty");
	}
}
