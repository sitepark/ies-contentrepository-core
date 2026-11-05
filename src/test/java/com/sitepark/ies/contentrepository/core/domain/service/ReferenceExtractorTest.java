package com.sitepark.ies.contentrepository.core.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sitepark.ies.contentrepository.core.domain.entity.Section;
import com.sitepark.ies.contentrepository.core.domain.entity.SectionData;
import com.sitepark.ies.contentrepository.core.domain.value.Reference;
import com.sitepark.ies.contentrepository.core.domain.value.ReferenceKind;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReferenceExtractorTest {

  private static final String ENTRY = "100560100000012345";
  private static final String LINKS = "c41b2a3c-4d5e-4f60-9a7b-8c9d0e1f2a3b";
  private static final String IMAGE = "e9d0a1b2-c3d4-4e5f-8a6b-7c8d9e0f1a2b";

  private static Section section(String uuid, String json) throws JsonProcessingException {
    ObjectNode data = (ObjectNode) new ObjectMapper().readTree(json);
    return Section.builder().uuid(uuid).type("t").data(SectionData.of(data)).build();
  }

  @Test
  void findsLinksInArraysOfObjects() throws JsonProcessingException {
    Section links =
        section(
            LINKS,
            """
            {"sp_link_iterate":[
              {"sp_linkText":"Formular","sp_url":"ies:entity/100560100000004711"},
              {"sp_linkText":"Satzung","sp_url":"https://www.kassel.de/satzung.pdf"}]}
            """);

    List<Reference> references = new ReferenceExtractor().extract(ENTRY, List.of(links));

    assertThat(references)
        .as("only the internal link should be a reference, with its JSON pointer")
        .containsExactly(
            new Reference(
                ENTRY,
                LINKS,
                "/sp_link_iterate/0/sp_url",
                "ies:entity/100560100000004711",
                ReferenceKind.LINK));
  }

  @Test
  void findsReferencesInSectionsBelowOtherSections() throws JsonProcessingException {
    Section image =
        section(IMAGE, "{\"sp_image\":\"ies:media/42\"}").toBuilder().container("items").build();
    Section parent =
        section(LINKS, "{\"sp_headline\":\"Images\"}").toBuilder()
            .children("items", List.of(image))
            .build();

    List<Reference> references = new ReferenceExtractor().extract(ENTRY, List.of(parent));

    assertThat(references)
        .as("the reference should belong to the child section holding the value")
        .containsExactly(
            new Reference(ENTRY, IMAGE, "/sp_image", "ies:media/42", ReferenceKind.MEDIA));
  }

  @Test
  void findsMedia() throws JsonProcessingException {
    Section image =
        section(
            IMAGE, "{\"sp_image\":\"ies:media/100560100000008800\",\"sp_imageAlign\":\"left\"}");

    List<Reference> references = new ReferenceExtractor().extract(ENTRY, List.of(image));

    assertThat(references)
        .as("the medium should be a reference of kind MEDIA")
        .containsExactly(
            new Reference(
                ENTRY, IMAGE, "/sp_image", "ies:media/100560100000008800", ReferenceKind.MEDIA));
  }

  @Test
  void findsReferencesInMultiValuedFields() throws JsonProcessingException {
    Section image = section(IMAGE, "{\"sp_tags\":[\"ies:entity/1\",\"ies:entity/2\"]}");

    List<Reference> references = new ReferenceExtractor().extract(ENTRY, List.of(image));

    assertThat(references)
        .as("each value of an array should be a reference of its own")
        .extracting(Reference::path)
        .containsExactly("/sp_tags/0", "/sp_tags/1");
  }

  @Test
  void escapesFieldNamesInPointer() throws JsonProcessingException {
    Section image = section(IMAGE, "{\"a/b\":{\"c~d\":\"ies:entity/1\"}}");

    List<Reference> references = new ReferenceExtractor().extract(ENTRY, List.of(image));

    assertThat(references)
        .as("slash and tilde should be escaped as in RFC 6901")
        .extracting(Reference::path)
        .containsExactly("/a~1b/c~0d");
  }

  @Test
  void ignoresPrefixWithoutId() throws JsonProcessingException {
    Section image = section(IMAGE, "{\"sp_url\":\"ies:entity/\",\"sp_text\":\"ies:media\"}");

    List<Reference> references = new ReferenceExtractor().extract(ENTRY, List.of(image));

    assertThat(references).as("a prefix without id should be no reference").isEmpty();
  }
}
