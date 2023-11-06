package com.sitepark.ies.contentrepository.core.domain.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ContentDifferTest {

	@Test
	void testDiff() {
		assertThrows(UnsupportedOperationException.class, () -> {
			ContentDiffer differ = new ContentDiffer();
			differ.diff(null, null);
		}, "should be unsupported");
	}

}
