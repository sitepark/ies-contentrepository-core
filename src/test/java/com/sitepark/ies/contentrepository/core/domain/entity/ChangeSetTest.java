package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ChangeSetTest {

  @Test
  void testIsEmpty() {
    ChangeSet changeSet = new ChangeSet();
    assertTrue(changeSet.isEmpty(), "changeSet should be empty");
  }
}
