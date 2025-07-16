package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class NotTest {
  @Test
  void testEquals() {
    EqualsVerifier.forClass(Not.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(Not.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
