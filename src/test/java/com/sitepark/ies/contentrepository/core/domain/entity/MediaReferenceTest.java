package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@SuppressFBWarnings({
  "PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
  "NP_NULL_PARAM_DEREF_NONVIRTUAL",
  "NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class MediaReferenceTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(MediaReference.class).verify();
  }

  @Test
  void testSetMediaId() {
    MediaReference ref = MediaReference.builder().mediaId("123").build();

    assertEquals("123", ref.getMediaId(), "unexpected mediaId");
  }

  @Test
  void testInvalidMediaId() {
    assertThrows(
        NullPointerException.class,
        () -> {
          MediaReference.builder().mediaId(null);
        },
        "mediaId should't be null");
  }

  @Test
  void testSetUsedBy() {
    MediaReference ref = MediaReference.builder().usedBy("123").build();

    assertEquals("123", ref.getUsedBy(), "unexpected usedBy");
  }

  @Test
  void testInvalidUsedBy() {
    assertThrows(
        NullPointerException.class,
        () -> {
          MediaReference.builder().usedBy(null);
        },
        "usedBy should't be null");
  }

  @Test
  void testSetType() {
    MediaReference ref = MediaReference.builder().type(MediaReferenceType.EMBEDDED).build();

    assertEquals(MediaReferenceType.EMBEDDED, ref.getType(), "unexpected type");
  }

  @Test
  void testSetNullType() {
    assertThrows(
        NullPointerException.class,
        () -> {
          MediaReference.builder().type(null);
        },
        "usedBy must not be null");
  }

  @Test
  void testToBuilder() {

    MediaReference ref =
        MediaReference.builder()
            .mediaId("123")
            .usedBy("345")
            .type(MediaReferenceType.EMBEDDED)
            .build();

    MediaReference copy = ref.toBuilder().usedBy("678").build();

    MediaReference expected =
        MediaReference.builder()
            .mediaId("123")
            .usedBy("678")
            .type(MediaReferenceType.EMBEDDED)
            .build();

    assertEquals(expected, copy, "unexpected mediaReference");
  }
}
