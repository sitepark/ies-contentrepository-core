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
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class SectionTest {

  private static final String UUID = "8a2e0f4c-7f1d-4c6b-9d3e-2b1a0c9d8e7f";
  private static final String CHILD_UUID = "c41b2a3c-4d5e-4f60-9a7b-8c9d0e1f2a3b";
  private static final String GRANDCHILD_UUID = "e9d0a1b2-c3d4-4e5f-8a6b-7c8d9e0f1a2b";

  private static Section child(String uuid, String container) {
    return Section.builder().uuid(uuid).type("text").container(container).build();
  }

  private static Section nested() {
    Section collapsible =
        Section.builder()
            .uuid(CHILD_UUID)
            .type("collapsible")
            .container("items")
            .children("items", List.of(child(GRANDCHILD_UUID, "items")))
            .build();
    return Section.builder()
        .uuid(UUID)
        .type("newSection")
        .container("sp_main_iterate")
        .children("items", List.of(collapsible))
        .build();
  }

  private static SectionData data(String text) {
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put("sp_text", text);
    return SectionData.of(node);
  }

  @Test
  void testEquals() {
    EqualsVerifier.forClass(Section.class)
        .withPrefabValues(SectionData.class, data("a"), data("b"))
        .withPrefabValues(
            Section.class,
            Section.builder().uuid(UUID).type("a").build(),
            Section.builder().uuid(CHILD_UUID).type("b").build())
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
  void holdsChildSectionsByContainer() {
    assertThat(nested().getChildren("items"))
        .as("the children of the container should be returned in their order")
        .extracting(Section::getUuid)
        .containsExactly(CHILD_UUID);
  }

  @Test
  void containerWithoutChildrenIsEmpty() {
    assertThat(nested().getChildren("other"))
        .as("a container without sections should hold no children")
        .isEmpty();
  }

  @Test
  void rejectsChildNamingAnotherContainer() {
    Section.Builder builder = Section.builder().uuid(UUID).type("newSection");
    List<Section> children = List.of(child(CHILD_UUID, "other"));

    assertThatThrownBy(() -> builder.children("items", children))
        .as("a child has to name the container it lies in")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void emptyChildListRemovesTheContainer() {
    Section section = nested().toBuilder().children("items", List.of()).build();

    assertThat(section.getContainers()).as("an empty container should be dropped").isEmpty();
  }

  @Test
  void streamsTheSectionAndAllSectionsBelowInTreeOrder() {
    assertThat(nested().stream())
        .as("each parent should come before its children")
        .extracting(Section::getUuid)
        .containsExactly(UUID, CHILD_UUID, GRANDCHILD_UUID);
  }

  @Test
  void serializationRoundTripKeepsNestedSections() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper().registerModule(new Jdk8Module());
    Section section = nested();

    Section read = mapper.readValue(mapper.writeValueAsString(section), Section.class);

    assertThat(read).as("nested sections should survive a JSON round trip").isEqualTo(section);
  }

  @Test
  void serializationOmitsEmptyContainers() throws JsonProcessingException {
    String json =
        new ObjectMapper()
            .registerModule(new Jdk8Module())
            .writeValueAsString(Section.builder().uuid(UUID).type("text").build());

    assertThat(json)
        .as("a section without children should have no containers")
        .doesNotContain("containers");
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
