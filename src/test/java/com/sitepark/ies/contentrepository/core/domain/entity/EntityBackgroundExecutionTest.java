package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Arrays;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@SuppressFBWarnings({
  "PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
  "NP_NULL_PARAM_DEREF_NONVIRTUAL",
  "NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class EntityBackgroundExecutionTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(EntityBackgroundExecution.class).verify();
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void testToString() {
    ToStringVerifier.forClass(EntityBackgroundExecution.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }

  @Test
  void testSetTopic() {
    EntityBackgroundOperation op = mock(EntityBackgroundOperation.class);
    EntityBackgroundExecution execution =
        EntityBackgroundExecution.builder().topic("topic").operation(op).build();

    String[] expected = new String[] {"topic"};

    assertArrayEquals(expected, execution.getTopic(), "unexpected topics");
  }

  @Test
  void testSetNullTopic() {
    assertThrows(
        NullPointerException.class,
        () -> {
          EntityBackgroundExecution.builder().topic((String[]) null);
        },
        "topic must not be null");
  }

  @Test
  void testSetNullTopicPart() {
    assertThrows(
        NullPointerException.class,
        () -> {
          EntityBackgroundExecution.builder().topic((String) null);
        },
        "topic part must not be null");
  }

  @Test
  void testMissingTopic() {
    assertThrows(
        IllegalStateException.class,
        () -> {
          EntityBackgroundOperation op = mock(EntityBackgroundOperation.class);
          EntityBackgroundExecution.builder().operation(op).build();
        },
        "topic must be set");
  }

  @Test
  void testSetOperation() {
    EntityBackgroundOperation op = mock(EntityBackgroundOperation.class);
    EntityBackgroundExecution execution =
        EntityBackgroundExecution.builder().topic("topic").operation(op).build();

    List<EntityBackgroundOperation> expected = Arrays.asList(op);

    assertEquals(expected, execution.getOperations(), "unexpected operations");
  }

  @Test
  void testSetNullOperation() {
    assertThrows(
        NullPointerException.class,
        () -> {
          EntityBackgroundExecution.builder().operation((EntityBackgroundOperation) null);
        },
        "operation must not be null");
  }

  @Test
  void testSetNullOperationArray() {
    assertThrows(
        NullPointerException.class,
        () -> {
          EntityBackgroundExecution.builder().operation((EntityBackgroundOperation[]) null);
        },
        "operations must not be null");
  }

  @Test
  void testMissingOperation() {
    assertThrows(
        IllegalStateException.class,
        () -> {
          EntityBackgroundExecution.builder().topic("topic").build();
        },
        "operation must be set");
  }

  @Test
  void testSetFinalizer() {
    EntityBackgroundOperation op = mock(EntityBackgroundOperation.class);
    EntityBackgroundOperation finalizer = mock(EntityBackgroundOperation.class);
    EntityBackgroundExecution execution =
        EntityBackgroundExecution.builder()
            .topic("topic")
            .operation(op)
            .finalizer(finalizer)
            .build();

    assertEquals(finalizer, execution.getFinalizer().get(), "unexpected finalizer");
  }

  @Test
  void testEmptyFinalizer() {
    EntityBackgroundOperation op = mock(EntityBackgroundOperation.class);
    EntityBackgroundExecution execution =
        EntityBackgroundExecution.builder().topic("topic").operation(op).build();

    assertTrue(execution.getFinalizer().isEmpty(), "finalizer optional should be empty");
  }
}
