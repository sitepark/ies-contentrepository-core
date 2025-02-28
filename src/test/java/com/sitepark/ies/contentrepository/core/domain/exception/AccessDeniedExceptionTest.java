package com.sitepark.ies.contentrepository.core.domain.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AccessDeniedExceptionTest {

  @Test
  void testConstructor() {
    AccessDeniedException e = new AccessDeniedException("message", new Exception());
    assertEquals("message", e.getMessage(), "unexpected message");
  }
}
