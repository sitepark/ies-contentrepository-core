package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BackgroundOperationKeyTest {

  @Test
  void testGetName() {
    assertEquals(
        "contentrepository.purge.lock",
        BackgroundOperationKey.PURGE_LOCK.getName(),
        "unexpected name");
  }
}
