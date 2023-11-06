package com.sitepark.ies.contentrepository.core.domain.entity.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class SubTreeQueryTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(SubTreeQuery.class)
			// see https://jqno.nl/equalsverifier/manual/inheritance/
			.withRedefinedSuperclass()
			.verify();
	}

	@Test
	void testSetRoot() {
		SubTreeQuery query = SubTreeQuery.builder()
				.root(123L)
				.build();
		List<Long> expected = Arrays.asList(123L);
		assertEquals(expected, query.getRootList(), "unexpected root");
	}

	@Test
	void testSetNullRoot() {
		assertThrows(NullPointerException.class, () -> {
			SubTreeQuery.builder().root(null);
		}, "root must not be null");
	}

	@Test
	void testSetRootList() {
		SubTreeQuery query = SubTreeQuery.builder()
				.rootList(Arrays.asList(123L))
				.build();
		List<Long> expected = Arrays.asList(123L);
		assertEquals(expected, query.getRootList(), "unexpected rootList");
	}

	@Test
	void testSetNullRootList() {
		assertThrows(NullPointerException.class, () -> {
			SubTreeQuery.builder().rootList(null);
		}, "rootList must not be null");
	}

	@Test
	void testSetNullRootListItem() {
		assertThrows(NullPointerException.class, () -> {
			SubTreeQuery.builder().rootList(Arrays.asList((Long)null));
		}, "rootList item must not be null");
	}

	@Test
	void testEmptyRootList() {
		assertThrows(IllegalStateException.class, () -> {
			SubTreeQuery.builder().build();
		}, "rootList must not be empty");
	}

	@Test
	void testToBuilderWithNewRootList() {
		SubTreeQuery query = SubTreeQuery.builder()
				.rootList(Arrays.asList(123L))
				.build();

		SubTreeQuery copy = query.toBuilder()
				.rootList(Arrays.asList(345L))
				.build();

		SubTreeQuery expected = SubTreeQuery.builder()
				.rootList(Arrays.asList(345L))
				.build();

		assertEquals(expected, copy, "unexpected copy");
	}
	@Test
	void testToBuilderAppendRoot() {
		SubTreeQuery query = SubTreeQuery.builder()
				.rootList(Arrays.asList(123L))
				.build();

		SubTreeQuery copy = query.toBuilder()
				.root(345L)
				.build();

		SubTreeQuery expected = SubTreeQuery.builder()
				.rootList(Arrays.asList(123L, 345L))
				.build();

		assertEquals(expected, copy, "unexpected copy");
	}
}
