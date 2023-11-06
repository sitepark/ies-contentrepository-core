package com.sitepark.ies.contentrepository.core.domain.entity.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class NameTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(Name.class)
			.verify();
	}

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	public void testToString() {
		ToStringVerifier.forClass(Name.class)
				.verify();
	}

	@Test
	void testSetName() {
		Name orderByName = Name.builder()
				.build();
		assertNotNull(orderByName, "build() should not return null");
	}

	@Test
	void testToBuilder() {

		Name orderByName = Name.builder()
				.build();

		Name copy = orderByName.toBuilder()
				.direction(Direction.ASC)
				.build();

		Name expected = Name.builder()
				.direction(Direction.ASC)
				.build();

		assertEquals(expected, copy, "unexpected copy");
	}

}
