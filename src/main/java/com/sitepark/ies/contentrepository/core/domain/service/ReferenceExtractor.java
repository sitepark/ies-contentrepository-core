package com.sitepark.ies.contentrepository.core.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sitepark.ies.contentrepository.core.domain.entity.Section;
import com.sitepark.ies.contentrepository.core.domain.value.Reference;
import com.sitepark.ies.contentrepository.core.domain.value.ReferenceKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Finds the references in the data of sections.
 *
 * <p>A reference is a string value of the form {@code ies:entity/<id>} (a link to an entry or a
 * group) or {@code ies:media/<id>} (a medium). The values are found without a schema, anywhere in
 * the data, in objects and arrays. Other URIs, e.g. external links, are no references.
 */
@SuppressWarnings("PMD.LawOfDemeter")
public final class ReferenceExtractor {

  static final String ENTITY_PREFIX = "ies:entity/";

  static final String MEDIA_PREFIX = "ies:media/";

  /**
   * Returns the references in the sections of an entry or a group, in the order of the sections.
   *
   * @param entityId the id of the entry or group holding the sections
   */
  public List<Reference> extract(String entityId, List<Section> sections) {
    Objects.requireNonNull(entityId, "entityId is null");
    Objects.requireNonNull(sections, "sections is null");
    List<Reference> references = new ArrayList<>();
    for (Section section : sections) {
      this.collect(entityId, section.getUuid(), "", section.getData().toJson(), references);
    }
    return List.copyOf(references);
  }

  private void collect(
      String entityId, String sectionUuid, String path, JsonNode node, List<Reference> result) {
    if (node.isObject()) {
      for (Map.Entry<String, JsonNode> field : node.properties()) {
        this.collect(
            entityId, sectionUuid, path + "/" + escape(field.getKey()), field.getValue(), result);
      }
    } else if (node.isArray()) {
      for (int i = 0; i < node.size(); i++) {
        this.collect(entityId, sectionUuid, path + "/" + i, node.get(i), result);
      }
    } else if (node.isTextual()) {
      String value = node.asText();
      ReferenceKind kind = kindOf(value);
      if (kind != null) {
        result.add(new Reference(entityId, sectionUuid, path, value, kind));
      }
    }
  }

  private static @Nullable ReferenceKind kindOf(String value) {
    if (value.startsWith(ENTITY_PREFIX) && value.length() > ENTITY_PREFIX.length()) {
      return ReferenceKind.LINK;
    }
    if (value.startsWith(MEDIA_PREFIX) && value.length() > MEDIA_PREFIX.length()) {
      return ReferenceKind.MEDIA;
    }
    return null;
  }

  /** Escapes a field name for a JSON pointer (RFC 6901). */
  private static String escape(String name) {
    return name.replace("~", "~0").replace("/", "~1");
  }
}
