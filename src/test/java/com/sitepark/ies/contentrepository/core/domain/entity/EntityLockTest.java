package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class EntityLockTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(EntityLock.class)
			.verify();
	}

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	public void testToString() {
		ToStringVerifier.forClass(EntityLock.class)
				.withClassName(NameStyle.SIMPLE_NAME)
				.verify();
	}

	@Test
	void testSetEntity() {
		EntityLock lock = EntityLock.builder()
			.entity(123)
			.build();

		assertEquals(123, lock.getEntity(), "unexpected entity");
	}

	@Test
	void testSetUser() {
		EntityLock lock = EntityLock.builder()
			.user(123)
			.build();

		assertEquals(123, lock.getUser(), "unexpected user");
	}

	@Test
	void testSetCreated() {
		EntityLock lock = EntityLock.builder()
			.created(123)
			.build();

		assertEquals(123, lock.getCreated(), "unexpected created");
	}

	@Test
	void testInvalidCreated() {
		assertThrows(IllegalArgumentException.class, () -> {
			EntityLock.builder().created(0);
		}, "created must be greater than 0");
	}

	@Test
	void testSetLastAccess() {
		EntityLock lock = EntityLock.builder()
			.lastAccess(123)
			.build();

		assertEquals(123, lock.getLastAccess(), "unexpected lastAccess");
	}

	@Test
	void testInvalidLastAccess() {
		assertThrows(IllegalArgumentException.class, () -> {
			EntityLock.builder().lastAccess(0);
		}, "lastAccess must be greater than 0");
	}

	@Test
	void testSetTtl() {
		EntityLock lock = EntityLock.builder()
			.ttl(123)
			.build();

		assertEquals(123, lock.getTtl(), "unexpected ttl");
	}

	@Test
	void testInvalidTtl() {
		assertThrows(IllegalArgumentException.class, () -> {
			EntityLock.builder().ttl(0);
		}, "ttl must be greater than 0");
	}

	@Test
	void testToBuilder() {

		EntityLock lock = EntityLock.builder()
				.entity(1)
				.user(2)
				.created(3)
				.lastAccess(4)
				.ttl(5)
				.build();

		EntityLock copy = lock.toBuilder()
				.user(10)
				.build();

		EntityLock expected = EntityLock.builder()
				.entity(1)
				.user(10)
				.created(3)
				.lastAccess(4)
				.ttl(5)
				.build();

		assertEquals(expected, copy, "unexpected entityLock");
	}

}
