package com.sitepark.ies.contentrepository.core.domain.entity.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class OrderCriteriaTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(OrderCriteria.class).verify();
  }

  @Test
  void testSetName() {
    OrderCriteria criteria = DummyOrderCriteria.builder().name("name").build();
    assertEquals("name", criteria.getName(), "unexpected name");
  }

  @Test
  void testSetDirection() {
    OrderCriteria criteria = DummyOrderCriteria.builder().direction(Direction.ASC).build();
    assertEquals(Direction.ASC, criteria.getDirection(), "unexpected direction");
  }

  @Test
  void testDefaultDirection() {
    OrderCriteria criteria = DummyOrderCriteria.builder().build();
    assertEquals(Direction.DESC, criteria.getDirection(), "unexpected direction");
  }

  @Test
  void testToBuilder() {
    OrderCriteria criteria =
        DummyOrderCriteria.builder().name("name").direction(Direction.ASC).build();

    OrderCriteria copy = criteria.toBuilder().name("name2").build();

    OrderCriteria expected =
        DummyOrderCriteria.builder().name("name2").direction(Direction.ASC).build();

    assertEquals(expected, copy, "unexpected copy");
  }
}
