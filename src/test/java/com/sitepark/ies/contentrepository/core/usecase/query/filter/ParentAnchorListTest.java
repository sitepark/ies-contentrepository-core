package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ParentAnchorListTest {
  @Test
  void testEquals() {
    EqualsVerifier.forClass(ParentAnchorList.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(ParentAnchorList.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
