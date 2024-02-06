package com.sitepark.ies.contentrepository.core.domain.entity.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter;
import com.sitepark.ies.contentrepository.core.domain.entity.sort.OrderBy;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.jqno.equalsverifier.EqualsVerifier;

@SuppressFBWarnings({
	"PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
	"NP_NULL_PARAM_DEREF_NONVIRTUAL",
	"NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class QueryTest {

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEqualsWithRedefinedCursorBasedQuery() {
		EqualsVerifier.forClass(Query.class)
			// see https://jqno.nl/equalsverifier/manual/inheritance/
			.withRedefinedSubclass(CursorBasedQuery.class)
			.verify();
	}

	@Test
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	void testEqualsWithRedefinedSubTreeQuery() {
		EqualsVerifier.forClass(Query.class)
			// see https://jqno.nl/equalsverifier/manual/inheritance/
			.withRedefinedSubclass(SubTreeQuery.class)
			.verify();
	}

	@Test
	void testSetFilter() {
		Filter filter = mock(Filter.class);
		Query query = Query.builder()
				.filterBy(filter)
				.build();
		assertEquals(filter, query.getFilterBy().get(), "unexpected filter");
	}

	@Test
	void testWithNullFilter() {
		Query query = Query.builder()
			.filterBy(null).build();
		assertTrue(query.getFilterBy().isEmpty(), "empty filter expected");
	}

	@Test
	void testSetOrderBy() {
		OrderBy orderBy = mock(OrderBy.class);
		Query query = Query.builder()
				.orderBy(orderBy)
				.build();
		assertEquals(orderBy, query.getOrderBy(), "unexpected orderBy");
	}

	@Test
	void testWithNullOrderBy() {
		assertThrows(NullPointerException.class, () -> {
			Query.builder().orderBy(null);
		}, "orderBy must not be null");
	}

	@Test
	void testSetOptions() {
		QueryOptions options = mock(QueryOptions.class);
		Query query = Query.builder()
				.options(options)
				.build();
		assertEquals(options, query.getOptions(), "unexpected options");
	}

	@Test
	void testWithNullOptions() {
		assertThrows(NullPointerException.class, () -> {
			Query.builder().options(null);
		}, "options must not be null");
	}

	@Test
	void testToBuilder() {

		Filter filter = mock(Filter.class);
		OrderBy orderBy = mock(OrderBy.class);
		QueryOptions options = mock(QueryOptions.class);

		Query query = Query.builder()
				.filterBy(filter)
				.orderBy(orderBy)
				.options(options)
				.build();

		Filter filter2 = mock(Filter.class);

		Query copy = query.toBuilder()
				.filterBy(filter2)
				.build();

		Query expected = Query.builder()
				.filterBy(filter2)
				.orderBy(orderBy)
				.options(options)
				.build();

		assertEquals(expected, copy, "unexpected copy");
	}
}
