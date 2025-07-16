package com.sitepark.ies.contentrepository.core.usecase.query.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class NameTest {

  @Test
  void testEquals() {
    EqualsVerifier.forClass(Name.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(Name.class).verify();
  }

  @Test
  void testConstructor() {
    Name name = new Name(Direction.ASC);
    assertEquals(Direction.ASC, name.getDirection(), "unexpected direction");
  }
}
