package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@SuppressFBWarnings({
  "PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES",
  "NP_NULL_PARAM_DEREF_NONVIRTUAL",
  "NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS"
})
class EntityLockTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(EntityLock.class).verify();
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void testToString() {
    ToStringVerifier.forClass(EntityLock.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  @Test
  void testSetEntity() {
    EntityLock lock = EntityLock.builder().entity("123").build();

    assertEquals("123", lock.getEntity(), "unexpected entity");
  }

  @Test
  void testSetEntityWithNull() {
    assertThrows(
        NullPointerException.class,
        () -> {
          EntityLock.builder().entity(null);
        },
        "entity should not be null");
  }

  @Test
  void testSetEntityWitZero() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          EntityLock.builder().entity("0");
        },
        "entity should not be zero");
  }

  @Test
  void testSetEntityWitInvalidValue() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          EntityLock.builder().entity("1x");
        },
        "entity should not be zero");
  }

  @Test
  void testSetUser() {
    EntityLock lock = EntityLock.builder().user("123").build();

    assertEquals("123", lock.getUser(), "unexpected user");
  }

  @Test
  void testSetUserWithNull() {
    assertThrows(
        NullPointerException.class,
        () -> {
          EntityLock.builder().user(null);
        },
        "user should not be null");
  }

  @Test
  void testSetUserWitZero() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          EntityLock.builder().user("0");
        },
        "user should not be zero");
  }

  @Test
  void testSetUserWitInvalidValue() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          EntityLock.builder().user("1x");
        },
        "user should not be zero");
  }

  @Test
  void testSetCreated() {
    OffsetDateTime created = OffsetDateTime.of(2024, 5, 2, 10, 10, 0, 0, ZoneOffset.UTC);
    EntityLock lock = EntityLock.builder().created(created).build();

    assertEquals(created, lock.getCreated(), "unexpected created");
  }

  @Test
  void testInvalidCreated() {
    assertThrows(
        NullPointerException.class,
        () -> {
          EntityLock.builder().created(null);
        },
        "null should not be allowed");
  }

  @Test
  void testSetLastAccess() {
    OffsetDateTime created = OffsetDateTime.of(2024, 5, 2, 10, 10, 0, 0, ZoneOffset.UTC);
    EntityLock lock = EntityLock.builder().lastAccess(created).build();

    assertEquals(created, lock.getLastAccess(), "unexpected lastAccess");
  }

  @Test
  void testInvalidLastAccess() {
    assertThrows(
        NullPointerException.class,
        () -> {
          EntityLock.builder().lastAccess(null);
        },
        "null should not be allowed");
  }

  @Test
  void testSetTtl() {
    EntityLock lock = EntityLock.builder().ttl(123).build();

    assertEquals(123, lock.getTtl(), "unexpected ttl");
  }

  @Test
  void testInvalidTtl() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          EntityLock.builder().ttl(0);
        },
        "ttl must be greater than 0");
  }

  @Test
  void testToBuilder() {

    OffsetDateTime created = OffsetDateTime.of(2024, 5, 2, 10, 10, 0, 0, ZoneOffset.UTC);

    OffsetDateTime lastAccess = OffsetDateTime.of(2024, 5, 2, 11, 10, 0, 0, ZoneOffset.UTC);

    EntityLock lock =
        EntityLock.builder()
            .entity("1")
            .user("2")
            .created(created)
            .lastAccess(lastAccess)
            .ttl(5)
            .build();

    EntityLock copy = lock.toBuilder().user("10").build();

    EntityLock expected =
        EntityLock.builder()
            .entity("1")
            .user("10")
            .created(created)
            .lastAccess(lastAccess)
            .ttl(5)
            .build();

    assertEquals(expected, copy, "unexpected entityLock");
  }
}
