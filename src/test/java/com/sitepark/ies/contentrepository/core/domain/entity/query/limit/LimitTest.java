package com.sitepark.ies.contentrepository.core.domain.entity.query.limit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LimitTest {

  @Test
  void test() {
    OffsetLimit offsetLimit = Limit.offset(1, 2);
    assertEquals(new OffsetLimit(1, 2), offsetLimit, "unexpected offset limit");
  }
}
