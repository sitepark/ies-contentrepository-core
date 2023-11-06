package com.sitepark.ies.contentrepository.core.domain.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.sitepark.ies.contentrepository.core.domain.entity.Anchor;

class AnchorAlreadyExistsExceptionTest {

	@Test
	void testGetAnchor() {
		Anchor anchor = Anchor.ofString("abc");
		AnchorAlreadyExistsException a = new AnchorAlreadyExistsException(anchor, 0);
		assertEquals(anchor, a.getAnchor(), "unexpected anchor");
	}

	@Test
	void testGetOwner() {
		AnchorAlreadyExistsException a = new AnchorAlreadyExistsException(null, 123);
		assertEquals(123, a.getOwner(), "unexpected owner");
	}

	@Test
	void testGetMessage() {
		Anchor anchor = Anchor.ofString("abc");
		AnchorAlreadyExistsException a = new AnchorAlreadyExistsException(anchor, 0);
		assertTrue(a.getMessage().contains(" abc "), "anchor missing in message");
	}

}
