package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class HistoryEntryTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(HistoryEntry.class)
			.verify();
	}

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	public void testToString() {
		ToStringVerifier.forClass(HistoryEntry.class)
				.withClassName(NameStyle.SIMPLE_NAME)
				.verify();
	}

	@Test
	void testSetEntity() {
		HistoryEntry entry = HistoryEntry.builder()
			.entity(123)
			.build();

		assertEquals(123, entry.getEntity(), "unexpected entry");
	}

	@Test
	void testSetInitiator() {
		HistoryEntry entry = HistoryEntry.builder()
			.initiator("initiator")
			.build();

		assertEquals("initiator", entry.getInitiator(), "unexpected initiator");
	}

	@Test
	void testSetNullInitiator() {
		assertThrows(NullPointerException.class, () -> {
			HistoryEntry.builder().initiator(null);
		}, "initiator must not be null");
	}

	@Test
	void testSetUser() {
		HistoryEntry entry = HistoryEntry.builder()
			.user(123L)
			.build();

		assertEquals(123, entry.getUser().get(), "unexpected user");
	}

	@Test
	void testEmptyUser() {
		HistoryEntry entry = HistoryEntry.builder()
			.build();

		assertTrue(entry.getUser().isEmpty(), "user should be empty");
	}


	@Test
	void testSetTimestamp() {
		HistoryEntry entry = HistoryEntry.builder()
			.timestamp(123L)
			.build();

		assertEquals(123, entry.getTimestamp(), "unexpected timestamp");
	}

	@Test
	void testSetInvalidTimestamp() {
		assertThrows(IllegalArgumentException.class, () -> {
			HistoryEntry.builder().timestamp(0);
		}, "timestamp must be greater than 0");
	}

	@Test
	void testSetType() {
		HistoryEntry entry = HistoryEntry.builder()
			.type(HistoryEntryType.CREATED)
			.build();

		assertEquals(HistoryEntryType.CREATED, entry.getType(), "unexpected type");
	}

	@Test
	void testSetNullType() {
		assertThrows(NullPointerException.class, () -> {
			HistoryEntry.builder().type(null);
		}, "type must not be null");
	}

	@Test
	void testSetComment() {
		HistoryEntry entry = HistoryEntry.builder()
			.comment("comment")
			.build();

		assertEquals("comment", entry.getComment(), "unexpected comment");
	}

	@Test
	void testSetNullComment() {
		assertThrows(NullPointerException.class, () -> {
			HistoryEntry.builder().comment(null);
		}, "comment must not be null");
	}

	@Test
	void testToBuilder() {
		HistoryEntry entry = HistoryEntry.builder()
				.entity(123)
				.initiator("initiator")
				.user(345L)
				.timestamp(123L)
				.type(HistoryEntryType.CREATED)
				.comment("comment")
				.build();

		HistoryEntry copy = entry.toBuilder()
				.entity(678)
				.build();

		HistoryEntry expected = HistoryEntry.builder()
				.entity(678)
				.initiator("initiator")
				.user(345L)
				.timestamp(123L)
				.type(HistoryEntryType.CREATED)
				.comment("comment")
				.build();

		assertEquals(expected, copy, "unexpected copy");
	}

}
