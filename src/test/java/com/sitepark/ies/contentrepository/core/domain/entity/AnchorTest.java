package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sitepark.ies.contentrepository.core.domain.exception.InvalidAnchorException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class AnchorTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(Anchor.class).verify();
  }

  @Test
  void testToString() {
    Anchor anchor = Anchor.ofString("abc");
    assertEquals("abc", anchor.toString(), "name of anchor expected");
  }

  @Test
  void testToStringForEmptyAnchor() {
    Anchor anchor = Anchor.EMPTY;
    assertEquals("EMPTY", anchor.toString(), "EMPTY expected");
  }

  @Test
  void testOfString() {
    Anchor anchor = Anchor.ofString("abc");
    assertEquals("abc", anchor.getName(), "expected name");
  }

  @Test
  void testOfStringWithNull() {
    Anchor anchor = Anchor.ofString(null);
    assertNull(anchor, "anchor should be null");
  }

  @Test
  void testOfStringWithBlank() {
    Anchor anchor = Anchor.ofString(" ");
    assertEquals(Anchor.EMPTY, anchor, "anchor should be empty");
  }

  @Test
  void testOfStringWithOnlyNumbers() {
    assertThrows(
        InvalidAnchorException.class,
        () -> {
          Anchor.ofString("123");
        },
        "anchor must not only contain numbers");
  }

  @Test
  void testOfStringWithContainsSpace() {
    assertThrows(
        InvalidAnchorException.class,
        () -> {
          Anchor.ofString("12 3");
        },
        "anchor must not contain spaces");
  }

  @Test
  void testOfStringWithAmp() {
    assertThrows(
        InvalidAnchorException.class,
        () -> {
          Anchor.ofString("12&3");
        },
        "anchor must not contain amp");
  }

  @Test
  void testOfStringWithOtherSpezialCharacter() {
    assertThrows(
        InvalidAnchorException.class,
        () -> {
          Anchor.ofString("12$3");
        },
        "anchor must not contain special characters");
  }
}
