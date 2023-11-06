package com.sitepark.ies.contentrepository.core.domain.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InvalidAnchorExceptionTest {

	@Test
	void testGetAnchor() {
		InvalidAnchorException a = new InvalidAnchorException("abc", null);
		assertEquals("abc", a.getName(), "unexpected name");
	}

	@Test
	void testGetMessage() {
		InvalidAnchorException a = new InvalidAnchorException("abc", null);
		assertTrue(a.getMessage().contains("'abc'"), "name missing in message");
	}
}
