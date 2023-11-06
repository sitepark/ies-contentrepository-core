package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class EntityBulkOperationTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(EntityBulkOperation.class)
			.verify();
	}

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	public void testToString() {
		ToStringVerifier.forClass(EntityBulkOperation.class)
				.withClassName(NameStyle.SIMPLE_NAME)
				.verify();
	}

	@Test
	void testSetKey() {
		Entity entity = mock();
		Consumer<Entity> consumer = mock();
		EntityBulkOperation op = EntityBulkOperation.builder()
			.key(BulkOperationKey.PURGE_LOCK)
			.entityList(Arrays.asList(entity))
			.consumer(consumer)
			.build();

		assertEquals(
				BulkOperationKey.PURGE_LOCK,
				op.getKey(),
				"unexpected key");
	}

	@Test
	void testSetNullKey() {
		assertThrows(NullPointerException.class, () -> {
			EntityBulkOperation.builder().key(null);
		}, "key must not be null");
	}

	@Test
	void testMissingKeys() {
		Entity entity = mock();
		Consumer<Entity> consumer = mock();
		assertThrows(IllegalStateException.class, () -> {
			EntityBulkOperation.builder()
				.entityList(Arrays.asList(entity))
				.consumer(consumer)
				.build();
		}, "key must not be missing");
	}

	@Test
	void testSetEntity() {
		Entity entity = mock();
		Consumer<Entity> consumer = mock();
		EntityBulkOperation op = EntityBulkOperation.builder()
			.key(BulkOperationKey.PURGE_LOCK)
			.entityList(Arrays.asList(entity))
			.consumer(consumer)
			.build();

		assertEquals(
				Arrays.asList(entity),
				op.getEntityList(),
				"unexpected entityLlist");
	}

	@Test
	void testSetNullEntityList() {
		assertThrows(NullPointerException.class, () -> {
			EntityBulkOperation.builder().entityList(null);
		}, "entityList must not be null");
	}

	@Test
	void testMissingEntity() {
		Consumer<Entity> consumer = mock();
		assertThrows(IllegalStateException.class, () -> {
			EntityBulkOperation.builder()
				.key(BulkOperationKey.PURGE_LOCK)
				.consumer(consumer)
				.build();
		}, "entity must not be missing");
	}

	@Test
	void testSetConsumer() {
		Entity entity = mock();
		Consumer<Entity> consumer = mock();
		EntityBulkOperation op = EntityBulkOperation.builder()
			.key(BulkOperationKey.PURGE_LOCK)
			.entityList(Arrays.asList(entity))
			.consumer(consumer)
			.build();

		assertEquals(
				consumer,
				op.getConsumer(),
				"unexpected consumer");
	}

	@Test
	void testSetNullConsumer() {
		assertThrows(NullPointerException.class, () -> {
			EntityBulkOperation.builder().consumer(null);
		}, "consumer must not be null");
	}

	@Test
	void testMissingComsumer() {
		Entity entity = mock();
		assertThrows(IllegalStateException.class, () -> {
			EntityBulkOperation.builder()
				.key(BulkOperationKey.PURGE_LOCK)
				.entityList(Arrays.asList(entity))
				.build();
		}, "entity must not be missing");
	}
}
