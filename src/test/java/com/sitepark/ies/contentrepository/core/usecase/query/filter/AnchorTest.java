package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import static org.junit.jupiter.api.Assertions.*;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class AnchorTest {
  @Test
  void testEquals() {
    EqualsVerifier.forClass(Anchor.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(Anchor.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
