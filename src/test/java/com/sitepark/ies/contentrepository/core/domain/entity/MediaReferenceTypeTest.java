package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MediaReferenceTypeTest {

  @Test
  void testName() {
    assertEquals("EMBEDDED", MediaReferenceType.EMBEDDED.name(), "unexpected name");
  }
}
