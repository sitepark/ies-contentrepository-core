package com.sitepark.ies.contentrepository.core.usecase.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class QueryOptionsTest {

  @Test
  void testEquals() {
    EqualsVerifier.forClass(QueryOptions.class).verify();
  }

  @Test
  void testSetShowHidden() {
    QueryOptions options = QueryOptions.builder().showHidden(true).build();
    assertTrue(options.isShowHidden(), "unexpected showHidden");
  }

  @Test
  void testToBuilder() {
    QueryOptions options = QueryOptions.builder().showHidden(true).build();

    QueryOptions copy = options.toBuilder().showHidden(false).build();

    QueryOptions expected = QueryOptions.builder().showHidden(false).build();

    assertEquals(expected, copy, "unexpected copy");
  }
}
