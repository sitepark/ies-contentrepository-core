package com.sitepark.ies.contentrepository.core.domain.entity.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class CursorBasedQueryTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(CursorBasedQuery.class)
			// see https://jqno.nl/equalsverifier/manual/inheritance/
			.withRedefinedSuperclass()
			.verify();
	}

	@Test
	void testSetName() {
		CursorBasedQuery query = CursorBasedQuery.builder()
				.first(3)
				.build();
		assertEquals(3, query.getFirst(), "unexpected first");
	}

	@Test
	void testSetAfter() {
		CursorBasedQuery query = CursorBasedQuery.builder()
				.after("test")
				.build();
		assertEquals("test", query.getAfter(), "unexpected after");
	}

	@Test
	void testToBuilder() {
		CursorBasedQuery query = CursorBasedQuery.builder()
				.first(3)
				.after("test")
				.build();

		CursorBasedQuery copy = query.toBuilder()
				.first(4)
				.build();

		CursorBasedQuery expected = CursorBasedQuery.builder()
				.first(4)
				.after("test")
				.build();

		assertEquals(expected, copy, "unexpected copy");
	}

}
