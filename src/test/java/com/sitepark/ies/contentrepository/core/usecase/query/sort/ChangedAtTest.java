package com.sitepark.ies.contentrepository.core.usecase.query.sort;

import static org.junit.jupiter.api.Assertions.*;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ChangedAtTest {
  @Test
  void testEquals() {
    EqualsVerifier.forClass(ChangedAt.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(ChangedAt.class).verify();
  }

  @Test
  void testConstructor() {
    ChangedAt changedAt = new ChangedAt(Direction.ASC);
    assertEquals(Direction.ASC, changedAt.getDirection(), "unexpected direction");
  }
}
