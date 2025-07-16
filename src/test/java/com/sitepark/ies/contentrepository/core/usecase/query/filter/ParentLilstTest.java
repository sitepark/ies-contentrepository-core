package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ParentLilstTest {
  @Test
  void testEquals() {
    EqualsVerifier.forClass(ParentList.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(ParentList.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
