package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class RootTest {
  @Test
  void testEquals() {
    EqualsVerifier.forClass(Root.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(Root.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
