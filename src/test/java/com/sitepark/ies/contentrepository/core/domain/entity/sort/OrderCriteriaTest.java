package com.sitepark.ies.contentrepository.core.domain.entity.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class OrderCriteriaTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(OrderCriteria.class)
			.verify();
	}

	@Test
	void testSetName() {
		OrderCriteria criteria = TestOrderCriteria.builder()
				.name("name")
				.build();
		assertEquals("name", criteria.getName(), "unexpected name");
	}

	@Test
	void testSetDirection() {
		OrderCriteria criteria = TestOrderCriteria.builder()
				.direction(Direction.ASC)
				.build();
		assertEquals(Direction.ASC, criteria.getDirection(), "unexpected direction");
	}

	@Test
	void testDefaultDirection() {
		OrderCriteria criteria = TestOrderCriteria.builder()
				.build();
		assertEquals(Direction.DESC, criteria.getDirection(), "unexpected direction");
	}

	@Test
	void testToBuilder() {
		OrderCriteria criteria = TestOrderCriteria.builder()
				.name("name")
				.direction(Direction.ASC)
				.build();

		OrderCriteria copy = criteria.toBuilder()
				.name("name2")
				.build();

		OrderCriteria expected = TestOrderCriteria.builder()
				.name("name2")
				.direction(Direction.ASC)
				.build();

		assertEquals(expected, copy, "unexpected copy");
	}
}
