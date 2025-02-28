package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Collections;
import java.util.function.Consumer;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressFBWarnings({
  "PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
  "NP_NULL_PARAM_DEREF_NONVIRTUAL",
  "NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class EntityBackgroundOperationTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(EntityBackgroundOperation.class).verify();
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testToString() {
    ToStringVerifier.forClass(EntityBackgroundOperation.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }

  @Test
  void testSetKey() {
    Entity entity = mock();
    Consumer<Entity> consumer = mock();
    EntityBackgroundOperation op =
        EntityBackgroundOperation.builder()
            .key(BackgroundOperationKey.PURGE_LOCK)
            .entityList(Collections.singletonList(entity))
            .consumer(consumer)
            .build();

    assertEquals(BackgroundOperationKey.PURGE_LOCK, op.getKey(), "unexpected key");
  }

  @Test
  void testSetNullKey() {
    assertThrows(
        NullPointerException.class,
        () -> EntityBackgroundOperation.builder().key(null),
        "key must not be null");
  }

  @Test
  void testMissingKeys() {
    Entity entity = mock();
    Consumer<Entity> consumer = mock();
    assertThrows(
        IllegalStateException.class,
        () ->
            EntityBackgroundOperation.builder()
                .entityList(Collections.singletonList(entity))
                .consumer(consumer)
                .build(),
        "key must not be missing");
  }

  @Test
  void testSetEntity() {
    Entity entity = mock();
    Consumer<Entity> consumer = mock();
    EntityBackgroundOperation op =
        EntityBackgroundOperation.builder()
            .key(BackgroundOperationKey.PURGE_LOCK)
            .entityList(Collections.singletonList(entity))
            .consumer(consumer)
            .build();

    assertEquals(Collections.singletonList(entity), op.getEntityList(), "unexpected entityList");
  }

  @Test
  void testSetNullEntityList() {
    assertThrows(
        NullPointerException.class,
        () -> EntityBackgroundOperation.builder().entityList(null),
        "entityList must not be null");
  }

  @Test
  void testMissingEntity() {
    Consumer<Entity> consumer = mock();
    assertThrows(
        IllegalStateException.class,
        () ->
            EntityBackgroundOperation.builder()
                .key(BackgroundOperationKey.PURGE_LOCK)
                .consumer(consumer)
                .build(),
        "entity must not be missing");
  }

  @Test
  void testSetConsumer() {
    Entity entity = mock();
    Consumer<Entity> consumer = mock();
    EntityBackgroundOperation op =
        EntityBackgroundOperation.builder()
            .key(BackgroundOperationKey.PURGE_LOCK)
            .entityList(Collections.singletonList(entity))
            .consumer(consumer)
            .build();

    assertEquals(consumer, op.getConsumer(), "unexpected consumer");
  }

  @Test
  void testSetNullConsumer() {
    assertThrows(
        NullPointerException.class,
        () -> EntityBackgroundOperation.builder().consumer(null),
        "consumer must not be null");
  }

  @Test
  void testMissingConsumer() {
    Entity entity = mock();
    assertThrows(
        IllegalStateException.class,
        () ->
            EntityBackgroundOperation.builder()
                .key(BackgroundOperationKey.PURGE_LOCK)
                .entityList(Collections.singletonList(entity))
                .build(),
        "entity must not be missing");
  }
}
