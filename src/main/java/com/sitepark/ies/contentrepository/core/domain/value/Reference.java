package com.sitepark.ies.contentrepository.core.domain.value;

import java.util.Objects;

/**
 * A reference from the data of a section to an entry, a group or a medium. References are derived
 * from the URI values of the section data when an entry is stored; they feed the dependencies,
 * the media repository and the link checker.
 *
 * @param entityId the id of the entry or group holding the section
 * @param sectionUuid the UUID of the section
 * @param path the JSON pointer of the value within the section data, e.g. {@code
 *     /sp_link_iterate/0/sp_url}
 * @param target the URI value, e.g. {@code ies:entity/123} or {@code ies:media/42}
 * @param kind the kind of the target
 */
public record Reference(
    String entityId, String sectionUuid, String path, String target, ReferenceKind kind) {

  public Reference {
    Objects.requireNonNull(entityId, "entityId is null");
    Objects.requireNonNull(sectionUuid, "sectionUuid is null");
    Objects.requireNonNull(path, "path is null");
    Objects.requireNonNull(target, "target is null");
    Objects.requireNonNull(kind, "kind is null");
  }

  /** The id of the referenced entry, group or medium, i.e. the part after the URI prefix. */
  public String targetId() {
    return this.target.substring(this.target.lastIndexOf('/') + 1);
  }
}
