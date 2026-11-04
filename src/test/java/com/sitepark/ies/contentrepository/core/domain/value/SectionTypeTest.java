package com.sitepark.ies.contentrepository.core.domain.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class SectionTypeTest {

  private static ObjectNode schema(String type) {
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put("type", type);
    return node;
  }

  @Test
  void testEquals() {
    EqualsVerifier.forClass(SectionType.class)
        .withPrefabValues(JsonNode.class, schema("object"), schema("string"))
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(SectionType.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withPrefabValue(JsonNode.class, schema("object"))
        .verify();
  }

  @Test
  void typeWithoutSchemaAndGrant() {
    SectionType type = SectionType.builder().id("text").origin("sitekit").build();

    assertThat(type.getSchema().isEmpty() && type.getGrant().isEmpty())
        .as("schema and grant should be optional")
        .isTrue();
  }

  @Test
  void changesOfTheSchemaDoNotAffectTheType() {
    ObjectNode schema = schema("object");
    SectionType type = SectionType.builder().id("text").origin("sitekit").schema(schema).build();

    schema.put("type", "string");

    assertThat(type.getSchema().map(s -> s.get("type").asText()))
        .as("the type should keep the schema it was created with")
        .contains("object");
  }

  @Test
  void requiresOrigin() {
    assertThatThrownBy(() -> SectionType.builder().id("text").build())
        .as("a type without origin should not be buildable")
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void rejectsBlankGrant() {
    assertThatThrownBy(() -> SectionType.builder().grant(" "))
        .as("a blank grant should be rejected")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void toBuilderCopiesAllFields() {
    SectionType type = SectionType.builder().id("teaser").origin("sitekit").grant("teaser").build();

    SectionType copy = type.toBuilder().origin("kassel").build();

    assertThat(copy)
        .as("the copy should only differ in the origin")
        .isEqualTo(SectionType.builder().id("teaser").origin("kassel").grant("teaser").build());
  }
}
