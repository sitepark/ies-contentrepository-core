package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HistoryEntryTypeTest {

  @Test
  void testName() {
    assertEquals("CREATED", HistoryEntryType.CREATED.name(), "unexpected name");
  }
}
