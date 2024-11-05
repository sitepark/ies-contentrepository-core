package com.sitepark.ies.contentrepository.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.sitepark.ies.contentrepository.core.domain.entity.filter.Filter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Arrays;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressFBWarnings({
  "PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
  "NP_NULL_PARAM_DEREF_NONVIRTUAL",
  "NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class BackgroundPurgeInputTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(BackgroundPurgeInput.class).verify();
  }

  @Test
  void testSetRootList() {

    BackgroundPurgeInput input =
        BackgroundPurgeInput.builder().rootList(Arrays.asList(123L)).build();
    assertEquals(Arrays.asList(123L), input.getRootList(), "unexpected rootList");
  }

  @Test
  void testNullSetRootList() {
    assertThrows(
        NullPointerException.class,
        () -> {
          BackgroundPurgeInput.builder().rootList(null);
        },
        "rootList must not be null");
  }

  @Test
  void testSetRoot() {

    BackgroundPurgeInput input = BackgroundPurgeInput.builder().root(123L).build();
    assertEquals(Arrays.asList(123L), input.getRootList(), "unexpected root");
  }

  @Test
  void testSetNullRoot() {
    assertThrows(
        NullPointerException.class,
        () -> {
          BackgroundPurgeInput.builder().root(null);
        },
        "root must not be null");
  }

  @Test
  void testSetFilter() {

    Filter filter = mock();

    BackgroundPurgeInput input = BackgroundPurgeInput.builder().filterBy(filter).build();
    assertEquals(filter, input.getFilter().get(), "unexpected root");
  }

  @Test
  void testSetNullFilter() {
    assertThrows(
        NullPointerException.class,
        () -> {
          BackgroundPurgeInput.builder().filterBy(null);
        },
        "filterBy must not be null");
  }

  @Test
  void testForceLock() {
    BackgroundPurgeInput input = BackgroundPurgeInput.builder().root(123L).forceLock(true).build();
    assertTrue(input.isForceLock(), "unexpected forceLock");
  }

  @Test
  void testRootAndFilterNotSet() {
    assertThrows(
        IllegalStateException.class,
        () -> {
          BackgroundPurgeInput.builder().build();
        });
  }

  @Test
  void testToBuilderWithNewRootList() {

    Filter filter = mock();

    BackgroundPurgeInput input =
        BackgroundPurgeInput.builder().root(123L).filterBy(filter).forceLock(true).build();

    BackgroundPurgeInput copy = input.toBuilder().rootList(Arrays.asList(345L)).build();

    BackgroundPurgeInput expected =
        BackgroundPurgeInput.builder().root(345L).filterBy(filter).forceLock(true).build();

    assertEquals(expected, copy, "unexpected copy");
  }

  @Test
  void testToBuilderAppendRootList() {

    Filter filter = mock();

    BackgroundPurgeInput input =
        BackgroundPurgeInput.builder().root(123L).filterBy(filter).forceLock(true).build();

    BackgroundPurgeInput copy = input.toBuilder().root(345L).build();

    BackgroundPurgeInput expected =
        BackgroundPurgeInput.builder()
            .rootList(Arrays.asList(123L, 345L))
            .filterBy(filter)
            .forceLock(true)
            .build();

    assertEquals(expected, copy, "unexpected copy");
  }
}
