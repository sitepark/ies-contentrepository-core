package com.sitepark.ies.contentrepository.core.domain.entity.query.limit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.sitepark.ies.contentrepository.core.usecase.query.limit.OffsetLimit;
import java.util.Optional;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class OffsetLimitTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEqualsWithRedefinedSubTreeQuery() {
    EqualsVerifier.forClass(OffsetLimit.class).verify();
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testToString() {
    ToStringVerifier.forClass(OffsetLimit.class).verify();
  }

  @Test
  void testNullOffset() {
    OffsetLimit offsetLimit = new OffsetLimit(null, null);
    assertEquals(0, offsetLimit.getOffset(), "unexpected offset");
  }

  @Test
  void testNullLimit() {
    OffsetLimit offsetLimit = new OffsetLimit(null, null);
    assertTrue(offsetLimit.getLimit().isEmpty(), "unexpected limit");
  }

  @Test
  void testOffset() {
    OffsetLimit offsetLimit = new OffsetLimit(1, 2);
    assertEquals(1, offsetLimit.getOffset(), "unexpected offset");
  }

  @Test
  void testLimit() {
    OffsetLimit offsetLimit = new OffsetLimit(1, 2);
    assertEquals(Optional.of(2), offsetLimit.getLimit(), "unexpected limit");
  }
}
