package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.util.LinkedHashMap;
import java.util.Map;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

@SuppressWarnings("PMD.UseConcurrentHashMap") // JSON objects of the tests
class SectionDataTest {

  private static Map<String, Object> json(String field, String value) {
    Map<String, Object> object = new LinkedHashMap<>();
    object.put(field, value);
    return object;
  }

  @Test
  void testEquals() {
    EqualsVerifier.forClass(SectionData.class)
        .withPrefabValues(Map.class, json("sp_text", "first"), json("sp_title", "second"))
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(SectionData.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withPrefabValue(Map.class, json("a", "1"))
        .verify();
  }

  @Test
  void changesOfTheSourceDoNotAffectTheData() {
    Map<String, Object> source = json("sp_text", "before");
    SectionData data = SectionData.of(source);

    source.put("sp_text", "after");

    assertThat(data.toMap().get("sp_text"))
        .as("the data should keep the value it was created with")
        .isEqualTo("before");
  }

  @Test
  void theHandedOutJsonCannotBeChanged() {
    SectionData data = SectionData.of(json("sp_text", "before"));

    assertThatThrownBy(() -> data.toMap().put("sp_text", "after"))
        .as("the handed out object should be immutable")
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void rejectsValuesThatAreNoJsonValues() {
    assertThatThrownBy(() -> SectionData.of(Map.of("date", new Object())))
        .as("only JSON values should be accepted")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void emptyDataHasNoFields() {
    assertThat(SectionData.empty().isEmpty()).as("empty data should have no fields").isTrue();
  }

  @Test
  void serializesAsPlainJsonObject() {
    SectionData data = SectionData.of(json("sp_text", "Hello"));

    String json =
        JsonMapper.builder()
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .build()
            .writeValueAsString(data);

    assertThat(json)
        .as("the data should be written as the JSON object itself")
        .isEqualTo("{\"sp_text\":\"Hello\"}");
  }

  @Test
  void deserializesFromPlainJsonObject() {
    String json =
        """
        {"sp_link_iterate":[{"sp_linkText":"Satzung","sp_url":"ies:entity/123"}],"sp_count":2}
        """;

    SectionData data =
        JsonMapper.builder()
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .build()
            .readValue(json, SectionData.class);

    assertThat(data.toMap())
        .as("nested objects and arrays should be read unchanged")
        .isEqualTo(
            JsonMapper.builder()
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .build()
                .readValue(json, Map.class));
  }
}
