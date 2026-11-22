package com.sitepark.ies.contentrepository.core.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.UseConcurrentHashMap") // JSON objects of the tests
class GroupTest {

  private static final String CONTACT = "1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d";

  private static SectionData data(String text) {
    Map<String, Object> node = new LinkedHashMap<>();
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
        .withPrefabValues(
            Section.class,
            Section.builder().uuid("8a2e0f4c-7f1d-4c6b-9d3e-2b1a0c9d8e7f").type("a").build(),
            Section.builder().uuid("c41b2a3c-4d5e-4f60-9a7b-8c9d0e1f2a3b").type("b").build())
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
}
