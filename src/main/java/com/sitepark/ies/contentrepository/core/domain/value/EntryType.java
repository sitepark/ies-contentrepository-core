package com.sitepark.ies.contentrepository.core.domain.value;

import java.util.Objects;

/**
 * The type of an entry. It decides what an entry can do; the content itself is defined by the
 * section types.
 *
 * @param id the id of the type, e.g. {@code page}, {@code category} or {@code ies:media}
 * @param publishable whether entries of the type are published
 * @param output what publishing produces
 * @param aggregatable whether entries of the type can be aggregated
 */
public record EntryType(String id, boolean publishable, Output output, boolean aggregatable) {

  /** The system type of entries that place a medium of the media repository in a group. */
  public static final String MEDIA = "ies:media";

  public EntryType {
    Objects.requireNonNull(id, "id is null");
    if (id.isBlank()) {
      throw new IllegalArgumentException("id is blank");
    }
    Objects.requireNonNull(output, "output is null");
  }
}
