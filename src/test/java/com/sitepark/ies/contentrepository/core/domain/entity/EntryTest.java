package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.sitepark.ies.contentrepository.core.domain.exception.SectionNotFoundException;
import java.time.Instant;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class EntryTest {

  private static final String TITLE = "3f1c0e2a-1b2c-4d3e-8f40-5a6b7c8d9e01";
  private static final String TEXT = "8a2e0f4c-7f1d-4c6b-9d3e-2b1a0c9d8e7f";
  private static final String IMAGE = "e9d0a1b2-c3d4-4e5f-8a6b-7c8d9e0f1a2b";
  private static final String LINKS = "c41b2a3c-4d5e-4f60-9a7b-8c9d0e1f2a3b";
  private static final String MAIN = "sp_main_iterate";

  private static SectionData data(String text) {
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put("sp_text", text);
    return SectionData.of(node);
  }

  private static Section section(String uuid, String type, String container) {
    return Section.builder().uuid(uuid).type(type).container(container).build();
  }

  private static Entry entry() {
    return Entry.builder()
        .type("page")
        .sections(
            List.of(
                Section.builder().uuid(TITLE).type("title").build(),
                section(TEXT, "text", MAIN),
                section(IMAGE, "image", MAIN)))
        .build();
  }

  private static List<String> uuids(Entry entry) {
    return entry.getSections().stream().map(Section::getUuid).toList();
  }

  @Test
  void testEquals() {
    EqualsVerifier.forClass(Entry.class)
        .withRedefinedSuperclass()
        .withPrefabValues(SectionData.class, data("a"), data("b"))
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(Entry.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withPrefabValue(SectionData.class, data("a"))
        .withIgnoredFields(
            "id",
            "anchor",
            "name",
            "parent",
            "version",
            "created",
            "createdBy",
            "changed",
            "changedBy",
            "group")
        .verify();
  }

  @Test
  void entryIsNoGroup() {
    assertThat(entry().isGroup()).as("an entry should never be a group").isFalse();
  }

  @Test
  void rejectsGroupFlag() {
    assertThatThrownBy(() -> Entry.builder().isGroup(true))
        .as("an entry should not be marked as group")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void requiresType() {
    assertThatThrownBy(() -> Entry.builder().build())
        .as("an entry without type should not be buildable")
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void rejectsDuplicateSectionUuids() {
    Section text = section(TEXT, "text", MAIN);
    assertThatThrownBy(() -> Entry.builder().sections(List.of(text, text)))
        .as("two sections with the same uuid should be rejected")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void entryWithDifferentSectionsIsNotEqual() {
    Entry changed = entry().withoutSection(IMAGE);

    assertThat(changed).as("sections should be part of the equality").isNotEqualTo(entry());
  }

  @Test
  void entryIsNotEqualToEntityWithSameMasterData() {
    Entry entry = Entry.builder().id("123").type("page").build();
    Entity entity = Entity.builder().id("123").build();

    assertThat(entity).as("an entity should never equal an entry").isNotEqualTo(entry);
  }

  @Test
  void addsSectionAtPositionWithinContainer() {
    Entry changed = entry().withSection(section(LINKS, "linkList", MAIN), 1);

    assertThat(uuids(changed))
        .as("the new section should be the second of its container")
        .containsExactly(TITLE, TEXT, LINKS, IMAGE);
  }

  @Test
  void addsSectionAtEndOfContainer() {
    Entry changed = entry().withSection(section(LINKS, "linkList", MAIN), 2);

    assertThat(uuids(changed))
        .as("the new section should follow the last section of its container")
        .containsExactly(TITLE, TEXT, IMAGE, LINKS);
  }

  @Test
  void addsSectionToNewContainerAtEnd() {
    Entry changed = entry().withSection(section(LINKS, "linkList", "sp_link_column_iterate"), 0);

    assertThat(uuids(changed))
        .as("a section of a container without sections should be appended")
        .containsExactly(TITLE, TEXT, IMAGE, LINKS);
  }

  @Test
  void rejectsAddingSectionOutsideContainer() {
    Section links = section(LINKS, "linkList", MAIN);
    assertThatThrownBy(() -> entry().withSection(links, 3))
        .as("a position behind the end of the container should be rejected")
        .isInstanceOf(IndexOutOfBoundsException.class);
  }

  @Test
  void rejectsAddingSectionWithExistingUuid() {
    Section duplicate = section(TEXT, "text", MAIN);
    assertThatThrownBy(() -> entry().withSection(duplicate, 0))
        .as("a section with an existing uuid should be rejected")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void removesSection() {
    Entry changed = entry().withoutSection(TEXT);

    assertThat(uuids(changed)).as("the section should be removed").containsExactly(TITLE, IMAGE);
  }

  @Test
  void rejectsRemovingUnknownSection() {
    assertThatThrownBy(() -> entry().withoutSection(LINKS))
        .as("removing an unknown section should fail")
        .isInstanceOf(SectionNotFoundException.class);
  }

  @Test
  void movesSectionWithinContainer() {
    Entry changed = entry().withSectionMovedTo(IMAGE, MAIN, 0);

    assertThat(uuids(changed))
        .as("the image should be the first section of the container")
        .containsExactly(TITLE, IMAGE, TEXT);
  }

  @Test
  void movesSectionIntoOtherContainer() {
    Entry changed = entry().withSectionMovedTo(TEXT, "sp_link_column_iterate", 0);

    assertThat(changed.getSection(TEXT).flatMap(Section::getContainer))
        .as("the moved section should belong to the target container")
        .contains("sp_link_column_iterate");
  }

  @Test
  void rejectsMovingUnknownSection() {
    assertThatThrownBy(() -> entry().withSectionMovedTo(LINKS, MAIN, 0))
        .as("moving an unknown section should fail")
        .isInstanceOf(SectionNotFoundException.class);
  }

  @Test
  void replacesSectionData() {
    Entry changed = entry().withSectionData(TEXT, data("Hello"));

    assertThat(changed.getSection(TEXT).map(Section::getData))
        .as("the section should have the new data")
        .contains(data("Hello"));
  }

  @Test
  void keepsOriginalWhenChanged() {
    Entry entry = entry();

    entry.withoutSection(TEXT);

    assertThat(uuids(entry))
        .as("the original entry should stay unchanged")
        .containsExactly(TITLE, TEXT, IMAGE);
  }

  @Test
  void toBuilderCopiesAllFields() {
    Entry entry =
        Entry.builder()
            .id("100560100000012345")
            .name("Hundesteuer")
            .type("page")
            .filename("hundesteuer.php")
            .version(Instant.parse("2026-10-28T09:14:02Z"))
            .build();

    Entry copy = entry.toBuilder().name("Dog tax").build();

    assertThat(copy)
        .as("the copy should only differ in the name")
        .isEqualTo(
            Entry.builder()
                .id("100560100000012345")
                .name("Dog tax")
                .type("page")
                .filename("hundesteuer.php")
                .version(Instant.parse("2026-10-28T09:14:02Z"))
                .build());
  }

  @Test
  void serializationRoundTripKeepsTheEntry() throws JsonProcessingException {
    ObjectMapper mapper =
        new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    Entry entry =
        entry().toBuilder()
            .id("100560100000012345")
            .anchor("leistung-hundesteuer")
            .name("Hundesteuer")
            .parent("100560100000000200")
            .filename("hundesteuer.php")
            .version(Instant.parse("2026-10-28T09:14:02Z"))
            .created(Instant.parse("2024-03-01T08:00:00Z"))
            .createdBy("17")
            .changed(Instant.parse("2026-10-28T09:14:02Z"))
            .changedBy("17")
            .build()
            .withSectionData(TEXT, data("<p>Wer einen Hund hält …</p>"));

    Entry read = mapper.readValue(mapper.writeValueAsString(entry), Entry.class);

    assertThat(read).as("the entry should survive a JSON round trip").isEqualTo(entry);
  }
}
