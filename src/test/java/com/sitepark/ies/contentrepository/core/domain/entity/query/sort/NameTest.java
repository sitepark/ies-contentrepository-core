package com.sitepark.ies.contentrepository.core.domain.entity.query.sort;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class NameTest {

	@Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(Name.class).verify();
	}

	@Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	public void testToString() {
		ToStringVerifier.forClass(Name.class).verify();
	}
}
