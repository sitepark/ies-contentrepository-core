package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.sitepark.ies.contentrepository.core.usecase.query.filter.Filter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressFBWarnings({
  "PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
  "NP_NULL_PARAM_DEREF_NONVIRTUAL",
  "NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class BackgroundPurgeInputTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(BackgroundPurgeInput.class).verify();
  }

  @Test
  void testNullSetRootList() {
    assertThrows(
        IllegalStateException.class,
        () -> BackgroundPurgeInput.builder().build(),
        "root must be set");
  }

  @Test
  void testSetFilter() {

    Filter filter = mock();

    BackgroundPurgeInput input = BackgroundPurgeInput.builder().filter(filter).build();
    assertEquals(filter, input.getFilter().orElse(null), "unexpected root");
  }

  @Test
  void testSetNullFilter() {
    assertThrows(
        NullPointerException.class,
        () -> BackgroundPurgeInput.builder().filter(null),
        "filterBy must not be null");
  }

  @Test
  void testForceLock() {
    BackgroundPurgeInput input =
        BackgroundPurgeInput.builder().filter(Filter.root("123")).forceLock(true).build();
    assertTrue(input.isForceLock(), "unexpected forceLock");
  }

  @Test
  void testRootAndFilterNotSet() {
    assertThrows(IllegalStateException.class, () -> BackgroundPurgeInput.builder().build());
  }

  @Test
  void testToBuilder() {
    BackgroundPurgeInput input =
        BackgroundPurgeInput.builder()
            .filter(Filter.root("123"))
            .forceLock(true)
            .build()
            .toBuilder()
            .filter(Filter.root("567"))
            .build();

    BackgroundPurgeInput expected =
        BackgroundPurgeInput.builder().filter(Filter.root("567")).forceLock(true).build();

    assertEquals(expected, input, "unexpected input after toBuilder() call");
  }
}
