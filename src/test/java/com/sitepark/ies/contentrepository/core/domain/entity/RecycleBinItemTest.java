package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Collections;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@SuppressFBWarnings({
  "PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
  "NP_NULL_PARAM_DEREF_NONVIRTUAL",
  "NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class RecycleBinItemTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    RecycleBinItem a = RecycleBinItem.builder().id("1").build();
    RecycleBinItem b = RecycleBinItem.builder().id("2").build();
    EqualsVerifier.forClass(RecycleBinItem.class)
        .withPrefabValues(RecycleBinItem.class, a, b)
        .verify();
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testToString() {
    ToStringVerifier.forClass(RecycleBinItem.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  @Test
  void testSetId() {
    RecycleBinItem item = RecycleBinItem.builder().id("123").build();

    assertEquals("123", item.getId(), "unexpected id");
  }

  @Test
  void testSetIdWithNull() {
    assertThrows(
        NullPointerException.class,
        () -> RecycleBinItem.builder().id(null),
        "id should not be null");
  }

  @Test
  void testSetIdWitZero() {
    assertThrows(
        IllegalArgumentException.class,
        () -> RecycleBinItem.builder().id("0"),
        "id should not be zero");
  }

  @Test
  void testSetIdWitInvalidValue() {
    assertThrows(
        IllegalArgumentException.class,
        () -> RecycleBinItem.builder().id("1x"),
        "id should not be zero");
  }

  @Test
  void testSetParent() {
    RecycleBinItem item = RecycleBinItem.builder().parent("123").build();

    assertEquals("123", item.getParent(), "unexpected parent");
  }

  @Test
  void testSetParentWithNull() {
    assertThrows(
        NullPointerException.class,
        () -> RecycleBinItem.builder().parent(null),
        "parent should not be null");
  }

  @Test
  void testSetParentWitZero() {
    assertThrows(
        IllegalArgumentException.class,
        () -> RecycleBinItem.builder().parent("0"),
        "parent should not be zero");
  }

  @Test
  void testSetParentWitInvalidValue() {
    assertThrows(
        IllegalArgumentException.class,
        () -> RecycleBinItem.builder().parent("1x"),
        "parent should not be zero");
  }

  @Test
  void testSetEntity() {
    Entity entity = mock();
    RecycleBinItem item = RecycleBinItem.builder().entity(entity).build();

    assertEquals(entity, item.getEntity(), "unexpected entity");
  }

  @Test
  void testSetChild() {
    RecycleBinItem child = mock(RecycleBinItem.class);
    RecycleBinItem item = RecycleBinItem.builder().child(child).build();
    List<RecycleBinItem> expected = Collections.singletonList(child);
    assertEquals(expected, item.getChildren(), "unexpected children");
  }

  @Test
  void testSetNullChild() {
    assertThrows(
        NullPointerException.class,
        () -> RecycleBinItem.builder().child(null),
        "child must not be null");
  }

  @Test
  void testSetChildren() {
    List<RecycleBinItem> children = Collections.singletonList(mock(RecycleBinItem.class));
    RecycleBinItem item = RecycleBinItem.builder().children(children).build();
    assertEquals(children, item.getChildren(), "unexpected children");
  }

  @Test
  void testSetNullChildren() {
    assertThrows(
        NullPointerException.class,
        () -> RecycleBinItem.builder().children(null),
        "children must not be null");
  }

  @Test
  void testToBuilder() {
    Entity entity = mock();
    List<RecycleBinItem> children = Collections.singletonList(mock(RecycleBinItem.class));
    RecycleBinItem item =
        RecycleBinItem.builder().id("123").parent("345").entity(entity).children(children).build();

    RecycleBinItem copy = item.toBuilder().parent("678").build();

    RecycleBinItem expected =
        RecycleBinItem.builder().id("123").parent("678").entity(entity).children(children).build();

    assertEquals(expected, copy, "unexpected copy");
  }
}
