package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ParentAnchorTest {
  @Test
  void testEquals() {
    EqualsVerifier.forClass(ParentAnchor.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(ParentAnchor.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
