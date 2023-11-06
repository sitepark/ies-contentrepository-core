package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BulkOperationKeyTest {

	@Test
	void testGetName() {
		assertEquals(
				"contentrepository.purge.lock",
				BulkOperationKey.PURGE_LOCK.getName(),
				"unexpected name");
	}
}
