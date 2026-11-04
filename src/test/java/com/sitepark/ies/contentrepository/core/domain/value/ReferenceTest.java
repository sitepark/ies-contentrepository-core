package com.sitepark.ies.contentrepository.core.domain.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class ReferenceTest {

  @Test
  void testEquals() {
    EqualsVerifier.forClass(Reference.class).suppress(Warning.NULL_FIELDS).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(Reference.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  @Test
  void targetIdIsThePartAfterThePrefix() {
    Reference reference =
        new Reference("1", "uuid", "/sp_url", "ies:entity/100560100000004711", ReferenceKind.LINK);

    assertThat(reference.targetId())
        .as("the target id should be the id of the referenced entry")
        .isEqualTo("100560100000004711");
  }

  @Test
  void rejectsMissingTarget() {
    assertThatThrownBy(() -> new Reference("1", "uuid", "/sp_url", null, ReferenceKind.LINK))
        .as("the target is required")
        .isInstanceOf(NullPointerException.class);
  }
}
