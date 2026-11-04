package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class SectionDataTest {

  private static ObjectNode json(String field, String value) {
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put(field, value);
    return node;
  }

  @Test
  void testEquals() {
    EqualsVerifier.forClass(SectionData.class)
        .withPrefabValues(ObjectNode.class, json("sp_text", "first"), json("sp_title", "second"))
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(SectionData.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withPrefabValue(ObjectNode.class, json("a", "1"))
        .verify();
  }

  @Test
  void changesOfTheSourceDoNotAffectTheData() {
    ObjectNode source = json("sp_text", "before");
    SectionData data = SectionData.of(source);

    source.put("sp_text", "after");

    assertThat(data.toJson().get("sp_text").asText())
        .as("the data should keep the value it was created with")
        .isEqualTo("before");
  }

  @Test
  void changesOfTheHandedOutJsonDoNotAffectTheData() {
    SectionData data = SectionData.of(json("sp_text", "before"));

    data.toJson().put("sp_text", "after");

    assertThat(data.toJson().get("sp_text").asText())
        .as("changing a handed out copy should not change the data")
        .isEqualTo("before");
  }

  @Test
  void emptyDataHasNoFields() {
    assertThat(SectionData.empty().isEmpty()).as("empty data should have no fields").isTrue();
  }

  @Test
  void serializesAsPlainJsonObject() throws JsonProcessingException {
    SectionData data = SectionData.of(json("sp_text", "Hello"));

    String json = new ObjectMapper().writeValueAsString(data);

    assertThat(json)
        .as("the data should be written as the JSON object itself")
        .isEqualTo("{\"sp_text\":\"Hello\"}");
  }

  @Test
  void deserializesFromPlainJsonObject() throws JsonProcessingException {
    String json =
        """
        {"sp_link_iterate":[{"sp_linkText":"Satzung","sp_url":"ies:entity/123"}],"sp_count":2}
        """;

    SectionData data = new ObjectMapper().readValue(json, SectionData.class);

    assertThat(data.toJson())
        .as("nested objects and arrays should be read unchanged")
        .isEqualTo(new ObjectMapper().readTree(json));
  }
}
