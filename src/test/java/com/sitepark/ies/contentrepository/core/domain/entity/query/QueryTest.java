package com.sitepark.ies.contentrepository.core.domain.entity.query;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.sitepark.ies.contentrepository.core.domain.entity.query.filter.Filter;
import com.sitepark.ies.contentrepository.core.domain.entity.query.limit.Limit;
import com.sitepark.ies.contentrepository.core.domain.entity.query.sort.SortCriteria;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressFBWarnings({
  "PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
  "NP_NULL_PARAM_DEREF_NONVIRTUAL",
  "NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class QueryTest {

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
    Query query = Query.builder().filterBy(filter).build();
    assertEquals(filter, query.getFilterBy().orElse(null), "unexpected filter");
  }

  @Test
  void testWithNullFilter() {
    Query query = Query.builder().filterBy(null).build();
    assertTrue(query.getFilterBy().isEmpty(), "empty filter expected");
  }

  @Test
  void testSetSort() {
    SortCriteria sortCriteria = mock(SortCriteria.class);
    Query query = Query.builder().sort(sortCriteria).build();
    assertEquals(List.of(sortCriteria), query.getSort(), "unexpected sort");
  }

  @Test
  void testSetSortAsArray() {
    SortCriteria[] sortCriteria = new SortCriteria[] {mock(SortCriteria.class)};
    Query query = Query.builder().sort(sortCriteria).build();
    assertEquals(List.of(sortCriteria), query.getSort(), "unexpected sort");
  }

  @Test
  void testSetSortAsCollection() {
    List<SortCriteria> sortCriteria = List.of(mock(SortCriteria.class));
    Query query = Query.builder().sort(sortCriteria).build();
    assertEquals(sortCriteria, query.getSort(), "unexpected sort");
  }

  @Test
  void testWithNullSort() {
    assertThrows(
        NullPointerException.class,
        () -> Query.builder().sort((SortCriteria) null),
        "orderBy must not be null");
  }

  @Test
  void testSetLimit() {
    Limit limit = mock(Limit.class);
    Query query = Query.builder().limit(limit).build();
    assertEquals(limit, query.getLimit(), "unexpected options");
  }

  @Test
  void testSetOptions() {
    QueryOptions options = mock(QueryOptions.class);
    Query query = Query.builder().options(options).build();
    assertEquals(options, query.getOptions(), "unexpected options");
  }

  @Test
  void testWithNullOptions() {
    assertThrows(
        NullPointerException.class,
        () -> Query.builder().options(null),
        "options must not be null");
  }

  @Test
  void testToBuilder() {

    Filter filter = mock(Filter.class);
    SortCriteria sortCriteria = mock(SortCriteria.class);
    QueryOptions options = mock(QueryOptions.class);

    Query query = Query.builder().filterBy(filter).sort(sortCriteria).options(options).build();

    Filter filter2 = mock(Filter.class);

    Query copy = query.toBuilder().filterBy(filter2).build();

    Query expected = Query.builder().filterBy(filter2).sort(sortCriteria).options(options).build();

    assertEquals(expected, copy, "unexpected copy");
  }
}
