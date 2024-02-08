package com.sitepark.ies.contentrepository.core.domain.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sitepark.ies.contentrepository.core.domain.entity.Anchor;
import org.junit.jupiter.api.Test;

class AnchorNotFoundExceptionTest {

  @Test
  void testGetAnchor() {
    Anchor anchor = Anchor.ofString("abc");
    AnchorNotFoundException a = new AnchorNotFoundException(anchor);
    assertEquals(anchor, a.getAnchor(), "unexpected anchor");
  }

  @Test
  void testGetMessage() {
    Anchor anchor = Anchor.ofString("abc");
    AnchorNotFoundException a = new AnchorNotFoundException(anchor);
    assertTrue(a.getMessage().contains(" abc "), "anchor missing in message");
  }
}
