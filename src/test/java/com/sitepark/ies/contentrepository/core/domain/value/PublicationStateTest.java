package com.sitepark.ies.contentrepository.core.domain.value;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.time.Instant;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class PublicationStateTest {

  @Test
  void testEquals() {
    EqualsVerifier.forClass(PublicationState.class).suppress(Warning.NULL_FIELDS).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(PublicationState.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  @Test
  void rejectsMissingVersion() {
    Instant now = Instant.parse("2026-11-04T10:00:00Z");
    assertThatThrownBy(() -> new PublicationState("1", "www", null, now, "17"))
        .as("the published version is required")
        .isInstanceOf(NullPointerException.class);
  }
}
