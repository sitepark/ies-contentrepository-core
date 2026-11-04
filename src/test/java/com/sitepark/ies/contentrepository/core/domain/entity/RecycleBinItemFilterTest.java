package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class RecycleBinItemFilterTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(RecycleBinItemFilter.class).verify();
  }

  @Test
  void testSetFrom() {
    Instant date = Instant.parse("2023-11-03T09:33:00Z");
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().from(date).build();

    assertEquals(date, filter.getFrom().get(), "unexpected from");
  }

  @Test
  void testEmptyFrom() {
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().build();
    assertTrue(filter.getFrom().isEmpty(), "from should be empty");
  }

  @Test
  void testSetTo() {
    Instant date = Instant.parse("2023-11-03T09:33:00Z");
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().to(date).build();

    assertEquals(date, filter.getTo().get(), "unexpected to");
  }

  @Test
  void testEmptyTo() {
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().build();
    assertTrue(filter.getTo().isEmpty(), "to should be empty");
  }

  @Test
  void testSetUser() {
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().user(123).build();

    assertEquals(123, filter.getUser().get(), "unexpected user");
  }

  @Test
  void testSetInvalidUser() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          RecycleBinItemFilter.builder().user(0);
        });
  }

  @Test
  void testEmptyUser() {
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().build();
    assertTrue(filter.getUser().isEmpty(), "user should be empty");
  }

  @Test
  void testToBuilder() {

    Instant from = Instant.parse("2023-11-03T09:33:00Z");
    Instant to = Instant.parse("2023-12-03T09:33:00Z");
    RecycleBinItemFilter filter =
        RecycleBinItemFilter.builder().from(from).to(to).user(123).build();

    RecycleBinItemFilter copy = filter.toBuilder().user(345).build();

    RecycleBinItemFilter expected =
        RecycleBinItemFilter.builder().from(from).to(to).user(345).build();

    assertEquals(expected, copy, "unexpected mediaReference");
  }
}
