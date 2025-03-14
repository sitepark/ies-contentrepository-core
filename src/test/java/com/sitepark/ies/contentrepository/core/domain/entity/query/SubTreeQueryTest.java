package com.sitepark.ies.contentrepository.core.domain.entity.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressFBWarnings({
  "PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
  "NP_NULL_PARAM_DEREF_NONVIRTUAL",
  "NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class SubTreeQueryTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(SubTreeQuery.class)
        // see https://jqno.nl/equalsverifier/manual/inheritance/
        .withRedefinedSuperclass()
        .verify();
  }

  @Test
  void testSetRoot() {
    SubTreeQuery query = SubTreeQuery.builder().root(123L).build();
    List<Long> expected = List.of(123L);
    assertEquals(expected, query.getRootList(), "unexpected root");
  }

  @Test
  void testSetNullRoot() {
    assertThrows(
        NullPointerException.class,
        () -> SubTreeQuery.builder().root(null),
        "root must not be null");
  }

  @Test
  void testSetRootList() {
    SubTreeQuery query = SubTreeQuery.builder().rootList(List.of(123L)).build();
    List<Long> expected = List.of(123L);
    assertEquals(expected, query.getRootList(), "unexpected rootList");
  }

  @Test
  void testSetNullRootList() {
    assertThrows(
        NullPointerException.class,
        () -> SubTreeQuery.builder().rootList(null),
        "rootList must not be null");
  }

  @Test
  void testSetNullRootListItem() {
    assertThrows(
        NullPointerException.class,
        () -> SubTreeQuery.builder().rootList(Collections.singletonList(null)),
        "rootList item must not be null");
  }

  @Test
  void testEmptyRootList() {
    assertThrows(
        IllegalStateException.class,
        () -> SubTreeQuery.builder().build(),
        "rootList must not be empty");
  }

  @Test
  void testToBuilderWithNewRootList() {
    SubTreeQuery query = SubTreeQuery.builder().rootList(List.of(123L)).build();

    SubTreeQuery copy = query.toBuilder().rootList(List.of(345L)).build();

    SubTreeQuery expected = SubTreeQuery.builder().rootList(List.of(345L)).build();

    assertEquals(expected, copy, "unexpected copy");
  }

  @Test
  void testToBuilderAppendRoot() {
    SubTreeQuery query = SubTreeQuery.builder().rootList(List.of(123L)).build();

    SubTreeQuery copy = query.toBuilder().root(345L).build();

    SubTreeQuery expected = SubTreeQuery.builder().rootList(Arrays.asList(123L, 345L)).build();

    assertEquals(expected, copy, "unexpected copy");
  }
}
