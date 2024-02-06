package com.sitepark.ies.contentrepository.core.domain.entity.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.jqno.equalsverifier.EqualsVerifier;

@SuppressFBWarnings({
	"PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
	"NP_NULL_PARAM_DEREF_NONVIRTUAL",
	"NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class OrderByTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEquals() {
		EqualsVerifier.forClass(OrderBy.class)
			.verify();
	}

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	public void testToString() {
		ToStringVerifier.forClass(OrderBy.class)
				.withClassName(NameStyle.SIMPLE_NAME)
				.verify();
	}

	@Test
	void testSetSort() {
		OrderCriteria criteria = mock(OrderCriteria.class);
		OrderBy orderBy = OrderBy.builder()
				.sort(criteria)
				.build();
		List<OrderCriteria> expected = Arrays.asList(criteria);
		assertEquals(expected, orderBy.getSort(), "unexpected sortlist");
	}

	@Test
	void testSetNullSortArray() {
		assertThrows(NullPointerException.class, () -> {
			OrderBy.builder().sort((OrderCriteria[])null);
		}, "sort array must not be null");
	}

	@Test
	void testSetNullSort() {
		assertThrows(NullPointerException.class, () -> {
			OrderBy.builder().sort((OrderCriteria)null);
		}, "sort must not be null");
	}

	@Test
	void testToBuilder() {
		OrderCriteria a = mock(OrderCriteria.class);
		OrderCriteria b = mock(OrderCriteria.class);
		OrderBy orderBy = OrderBy.builder()
				.sort(a)
				.build();

		OrderBy copy = orderBy.toBuilder()
				.sort(b)
				.build();

		OrderBy expected = OrderBy.builder()
				.sort(a)
				.sort(b)
				.build();

		assertEquals(expected, copy, "unexpected copy");
	}

}
