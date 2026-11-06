package com.sitepark.ies.contentrepository.core.domain.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.time.Instant;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class PublicationStateTest {

  private static final Instant VERSION = Instant.parse("2026-11-04T10:00:00Z");

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
    assertThatThrownBy(() -> new PublicationState("1", "www", true, null, VERSION, "17"))
        .as("the published version is required")
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void userAndTimeAreOptional() {
    PublicationState state = new PublicationState("1", "www", false, VERSION, null, null);

    assertThat(state)
        .as("a state without time and user should report them as absent")
        .extracting(s -> s.getPublishedAt().isPresent(), s -> s.getPublishedBy().isPresent())
        .containsExactly(false, false);
  }
}
