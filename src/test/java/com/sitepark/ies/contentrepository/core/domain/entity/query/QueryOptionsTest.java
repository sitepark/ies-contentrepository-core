package com.sitepark.ies.contentrepository.core.domain.entity.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class QueryOptionsTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(QueryOptions.class).verify();
  }

  @Test
  void testSetShowHidden() {
    QueryOptions options = QueryOptions.builder().showHidden(true).build();
    assertEquals(true, options.isShowHidden(), "unexpected showHidden");
  }

  @Test
  void testToBuilder() {
    QueryOptions options = QueryOptions.builder().showHidden(true).build();

    QueryOptions copy = options.toBuilder().showHidden(false).build();

    QueryOptions expected = QueryOptions.builder().showHidden(false).build();

    assertEquals(expected, copy, "unexpected copy");
  }
}
