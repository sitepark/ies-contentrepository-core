package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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
    LocalDateTime date = LocalDateTime.of(2023, 11, 3, 9, 33);
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().from(date).build();

    assertEquals(date, filter.getFrom().orElse(null), "unexpected from");
  }

  @Test
  void testEmptyFrom() {
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().build();
    assertTrue(filter.getFrom().isEmpty(), "from should be empty");
  }

  @Test
  void testSetTo() {
    LocalDateTime date = LocalDateTime.of(2023, 11, 3, 9, 33);
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().to(date).build();

    assertEquals(date, filter.getTo().orElse(null), "unexpected to");
  }

  @Test
  void testEmptyTo() {
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().build();
    assertTrue(filter.getTo().isEmpty(), "to should be empty");
  }

  @Test
  void testSetUser() {
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().userId("123").build();

    assertEquals("123", filter.getUserId().orElse(null), "unexpected user");
  }

  @Test
  void testSetInvalidUser() {
    assertThrows(NullPointerException.class, () -> RecycleBinItemFilter.builder().userId(null));
  }

  @Test
  void testEmptyUser() {
    RecycleBinItemFilter filter = RecycleBinItemFilter.builder().build();
    assertTrue(filter.getUserId().isEmpty(), "user should be empty");
  }

  @Test
  void testToBuilder() {

    LocalDateTime from = LocalDateTime.of(2023, 11, 3, 9, 33);
    LocalDateTime to = LocalDateTime.of(2023, 12, 3, 9, 33);
    RecycleBinItemFilter filter =
        RecycleBinItemFilter.builder().from(from).to(to).userId("123").build();

    RecycleBinItemFilter copy = filter.toBuilder().userId("345").build();

    RecycleBinItemFilter expected =
        RecycleBinItemFilter.builder().from(from).to(to).userId("345").build();

    assertEquals(expected, copy, "unexpected mediaReference");
  }
}
