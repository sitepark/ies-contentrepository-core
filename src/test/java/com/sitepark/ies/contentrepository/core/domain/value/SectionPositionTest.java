package com.sitepark.ies.contentrepository.core.domain.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class SectionPositionTest {

  private static final String PARENT = "8a2e0f4c-7f1d-4c6b-9d3e-2b1a0c9d8e7f";

  @Test
  void testEquals() {
    EqualsVerifier.forClass(SectionPosition.class).suppress(Warning.NULL_FIELDS).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(SectionPosition.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withPrefabValue(String.class, "items")
        .verify();
  }

  @Test
  void topLevelPositionHasNoParent() {
    assertThat(SectionPosition.topLevel("sp_main_iterate", 0).getParent())
        .as("a position on the top level should have no parent")
        .isEmpty();
  }

  @Test
  void fixedSectionsHaveNoContainer() {
    assertThat(SectionPosition.topLevel(null, 0).getContainer())
        .as("the fixed sections on the top level should have no container")
        .isEmpty();
  }

  @Test
  void positionWithinASectionNamesParentAndContainer() {
    assertThat(SectionPosition.within(PARENT, "items", 2))
        .as("the position should hold parent, container and index")
        .isEqualTo(new SectionPosition(PARENT, "items", 2));
  }

  @Test
  void rejectsNegativeIndex() {
    assertThatThrownBy(() -> SectionPosition.topLevel("sp_main_iterate", -1))
        .as("a negative index should be rejected")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void rejectsParentWithoutContainer() {
    assertThatThrownBy(() -> new SectionPosition(PARENT, null, 0))
        .as("a position within a section needs a container")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void rejectsBlankContainer() {
    assertThatThrownBy(() -> SectionPosition.topLevel(" ", 0))
        .as("a blank container should be rejected")
        .isInstanceOf(IllegalArgumentException.class);
  }
}
