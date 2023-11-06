package com.sitepark.ies.contentrepository.core.domain.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EntityNotFoundExceptionTest {

	@Test
	void testGetLock() {
		EntityNotFoundException a = new EntityNotFoundException(123L);
		assertEquals(123L, a.getId(), "unexpected id");
	}

	@Test
	void testGetMessage() {
		EntityNotFoundException a = new EntityNotFoundException(123L);
		assertTrue(a.getMessage().contains("123"), "id expected in message");
	}
}
