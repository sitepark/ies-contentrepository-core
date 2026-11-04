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
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class GroupTest {

  private static final String CONTACT = "1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d";

  private static SectionData data(String text) {
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put("sp_text", text);
    return SectionData.of(node);
  }

  private static Group group() {
    return Group.builder()
        .id("100560100000000200")
        .name("Bürgerservice")
        .type("department")
        .directory("buergerservice")
        .sections(List.of(Section.builder().uuid(CONTACT).type("contact").build()))
        .build();
  }

  @Test
  void testEquals() {
    EqualsVerifier.forClass(Group.class)
        .withRedefinedSuperclass()
        .withPrefabValues(SectionData.class, data("a"), data("b"))
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(Group.class)
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
  void groupIsAlwaysGroup() {
    assertThat(Group.builder().build().isGroup()).as("a group should be a group").isTrue();
  }

  @Test
  void rejectsClearingGroupFlag() {
    assertThatThrownBy(() -> Group.builder().isGroup(false))
        .as("a group should not be marked as entry")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void groupCarriesSections() {
    Group changed = group().withSectionData(CONTACT, data("Rathaus"));

    assertThat(changed.getSection(CONTACT).map(Section::getData))
        .as("a group should hold content in sections like an entry")
        .contains(data("Rathaus"));
  }

  @Test
  void groupIsNotEqualToEntryWithSameMasterData() {
    Entry entry = Entry.builder().id("123").type("page").build();
    Group group = Group.builder().id("123").build();

    assertThat(group).as("a group should never equal an entry").isNotEqualTo(entry);
  }

  @Test
  void serializationRoundTripKeepsTheGroup() throws JsonProcessingException {
    ObjectMapper mapper =
        new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    Group read = mapper.readValue(mapper.writeValueAsString(group()), Group.class);

    assertThat(read).as("the group should survive a JSON round trip").isEqualTo(group());
  }
}
