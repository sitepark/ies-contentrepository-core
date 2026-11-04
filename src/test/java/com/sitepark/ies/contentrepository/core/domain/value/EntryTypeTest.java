package com.sitepark.ies.contentrepository.core.domain.value;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class EntryTypeTest {

  @Test
  void testEquals() {
    EqualsVerifier.forClass(EntryType.class).suppress(Warning.NULL_FIELDS).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(EntryType.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  @Test
  void rejectsBlankId() {
    assertThatThrownBy(() -> new EntryType(" ", true, Output.FILE, true))
        .as("a blank id should be rejected")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void rejectsMissingOutput() {
    assertThatThrownBy(() -> new EntryType("page", true, null, true))
        .as("the output is required")
        .isInstanceOf(NullPointerException.class);
  }
}
