package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class SectionTest {

  private static final String UUID = "8a2e0f4c-7f1d-4c6b-9d3e-2b1a0c9d8e7f";

  private static SectionData data(String text) {
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put("sp_text", text);
    return SectionData.of(node);
  }

  @Test
  void testEquals() {
    EqualsVerifier.forClass(Section.class)
        .withPrefabValues(SectionData.class, data("a"), data("b"))
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(Section.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withPrefabValue(SectionData.class, data("a"))
        .verify();
  }

  @Test
  void buildsSectionWithAllFields() {
    Section section =
        Section.builder()
            .uuid(UUID)
            .type("text")
            .container("sp_main_iterate")
            .data(data("Hello"))
            .build();

    assertThat(section)
        .as("all fields should be set")
        .extracting(
            Section::getUuid,
            Section::getType,
            s -> s.getContainer().orElse(null),
            Section::getData)
        .containsExactly(UUID, "text", "sp_main_iterate", data("Hello"));
  }

  @Test
  void sectionWithoutDataHasEmptyData() {
    Section section = Section.builder().uuid(UUID).type("text").build();

    assertThat(section.getData()).as("data should default to empty").isEqualTo(SectionData.empty());
  }

  @Test
  void fixedSectionHasNoContainer() {
    Section section = Section.builder().uuid(UUID).type("title").build();

    assertThat(section.getContainer()).as("a fixed section has no container").isEmpty();
  }

  @Test
  void rejectsInvalidUuid() {
    assertThatThrownBy(() -> Section.builder().uuid("no-uuid"))
        .as("only UUIDs should be accepted")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void rejectsBlankType() {
    assertThatThrownBy(() -> Section.builder().type(" "))
        .as("a blank type should be rejected")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void rejectsBlankContainer() {
    assertThatThrownBy(() -> Section.builder().container(" "))
        .as("a blank container should be rejected")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void requiresUuid() {
    assertThatThrownBy(() -> Section.builder().type("text").build())
        .as("a section without uuid should not be buildable")
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void requiresType() {
    assertThatThrownBy(() -> Section.builder().uuid(UUID).build())
        .as("a section without type should not be buildable")
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void toBuilderCopiesAllFields() {
    Section section =
        Section.builder().uuid(UUID).type("text").container("sp_main_iterate").build();

    Section copy = section.toBuilder().data(data("changed")).build();

    assertThat(copy)
        .as("the copy should only differ in the data")
        .isEqualTo(
            Section.builder()
                .uuid(UUID)
                .type("text")
                .container("sp_main_iterate")
                .data(data("changed"))
                .build());
  }

  @Test
  void serializationRoundTripKeepsTheSection() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper().registerModule(new Jdk8Module());
    Section section =
        Section.builder()
            .uuid(UUID)
            .type("text")
            .container("sp_main_iterate")
            .data(data("Hello"))
            .build();

    Section read = mapper.readValue(mapper.writeValueAsString(section), Section.class);

    assertThat(read).as("the section should survive a JSON round trip").isEqualTo(section);
  }
}
