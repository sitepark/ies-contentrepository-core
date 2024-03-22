package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
class EntityTest {

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  void testEquals() {
    EqualsVerifier.forClass(Entity.class).verify();
  }

  @Test
  @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void testToString() {
    ToStringVerifier.forClass(Entity.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  @Test
  void testSetId() {
    Entity entity = Entity.builder().id("123").build();
    assertEquals("123", entity.getId().get(), "unexpected id");
  }

  @Test
  void testSetIdWithNull() {
    assertThrows(
        NullPointerException.class,
        () -> {
          Entity.builder().id(null);
        },
        "id should not be null");
  }

  @Test
  void testSetIdWithZero() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Entity.builder().id("0");
        },
        "id should not be zero");
  }

  @Test
  void testSetIdWithInvalidValid() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Entity.builder().id("0x");
        },
        "id should not be invalid");
  }

  @Test
  void testGetEmptyId() {
    Entity entity = Entity.builder().build();
    assertTrue(entity.getId().isEmpty(), "id optional should be empty");
  }

  @Test
  void testSetAnchorAsString() {
    Entity entity = Entity.builder().anchor("abc").build();
    assertEquals(Anchor.ofString("abc"), entity.getAnchor().get(), "unexpected anchor");
  }

  @Test
  void testSetAnchorWithNullString() {
    assertThrows(
        NullPointerException.class,
        () -> {
          Entity.builder().anchor((String) null);
        },
        "anchor should not be allowed to be null");
  }

  @Test
  void testSetAnchorAsAnchor() {
    Entity entity = Entity.builder().anchor(Anchor.ofString("abc")).build();
    assertEquals(Anchor.ofString("abc"), entity.getAnchor().get(), "unexpected anchor");
  }

  @Test
  void testSetAnchorWithNullAnchor() {
    assertThrows(
        NullPointerException.class,
        () -> {
          Entity.builder().anchor((String) null);
        },
        "anchor should not be allowed to be null");
  }

  @Test
  void testSetName() {
    Entity entity = Entity.builder().name("abc").build();
    assertEquals("abc", entity.getName().get(), "unexpected name");
  }

  @Test
  void testSetNameWithNull() {
    assertThrows(
        NullPointerException.class,
        () -> {
          Entity.builder().name(null);
        },
        "name should not be allowed to be null");
  }

  @Test
  void testSetNameWithBlank() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Entity.builder().name(" ");
        },
        "name should not be allowed to be blank");
  }

  @Test
  void testSetParent() {
    Entity entity = Entity.builder().parent("123").build();
    assertEquals("123", entity.getParent().get(), "unexpected parent");
  }

  @Test
  void testSetParentWithNull() {
    assertThrows(
        NullPointerException.class,
        () -> {
          Entity.builder().parent(null);
        },
        "parent should not be null");
  }

  @Test
  void testSetParentWithZero() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Entity.builder().parent("0");
        },
        "parent should not be zero");
  }

  @Test
  void testSetParentWithInvalidValid() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Entity.builder().parent("0x");
        },
        "parent should not be invalid");
  }

  @Test
  void testGetEmptyParent() {
    Entity entity = Entity.builder().build();
    assertTrue(entity.getParent().isEmpty(), "id optional should be empty");
  }

  @Test
  void testSetVersion() {
    OffsetDateTime version = OffsetDateTime.of(2024, 5, 2, 10, 10, 0, 0, ZoneOffset.UTC);
    Entity entity = Entity.builder().version(version).build();
    assertEquals(version, entity.getVersion().get(), "unexpected version");
  }

  @Test
  void testGetEmptyVersion() {
    Entity entity = Entity.builder().build();
    assertTrue(entity.getVersion().isEmpty(), "id optional should be empty");
  }

  @Test
  void testSetIsGroup() {
    Entity entity = Entity.builder().isGroup(true).build();
    assertTrue(entity.isGroup(), "isGroup should be true");
  }

  @Test
  void testToBuilder() {
    OffsetDateTime version = OffsetDateTime.of(2024, 5, 2, 10, 10, 0, 0, ZoneOffset.UTC);
    Entity entity =
        Entity.builder()
            .id("100560100000014842")
            .name("060 Rathaus")
            .parent("100560100000014840")
            .version(version)
            .isGroup(false)
            .build();

    Entity copy = entity.toBuilder().name("abc").build();

    Entity expected =
        Entity.builder()
            .id("100560100000014842")
            .name("abc")
            .parent("100560100000014840")
            .version(version)
            .isGroup(false)
            .build();

    assertEquals(expected, copy, "unexpected copy values");
  }

  @Test
  void testSerialize() throws JsonProcessingException {

    OffsetDateTime version = OffsetDateTime.of(2024, 5, 2, 10, 10, 0, 0, ZoneOffset.UTC);

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    Entity entity =
        Entity.builder()
            .id("100560100000014842")
            .name("060 Rathaus")
            .parent("100560100000014840")
            .version(version)
            .isGroup(false)
            .build();

    String json = mapper.writeValueAsString(entity);

    String expected =
        "{\"id\":\"100560100000014842\",\"anchor\":null,"
            + "\"name\":\"060 Rathaus\",\"parent\":\"100560100000014840\","
            + "\"version\":\"2024-05-02T10:10:00Z\",\"group\":false}";

    assertEquals(expected, json, "unexpected json");
  }
}
