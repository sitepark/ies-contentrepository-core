package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.sitepark.ies.contentrepository.core.usecase.query.filter.Filter;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class BackgroundPurgeInputTest {

  @Test
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
