package com.sitepark.ies.contentrepository.core.usecase.query.filter;

import static org.junit.jupiter.api.Assertions.*;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class NameTest {
  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(Name.class).verify();
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testToString() {
    ToStringVerifier.forClass(Name.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  @Test
  void testNullName() {
    assertThrows(
        NullPointerException.class,
        () -> {
          new Name(null);
        });
  }
}
