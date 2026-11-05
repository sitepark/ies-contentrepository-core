package com.sitepark.ies.contentrepository.core.domain.value;

import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

/**
 * The place of a section in the tree of sections of an entry or a group.
 *
 * @param parent the UUID of the section whose container holds the section; {@code null} for the
 *     top level of the entry or group
 * @param container the name of the container; {@code null} on the top level for the fixed sections
 * @param index the position within the container, starting with 0
 */
public record SectionPosition(@Nullable String parent, @Nullable String container, int index) {

  /**
   * Checks the position.
   *
   * @throws IllegalArgumentException if the index is negative, a name is blank, or a parent is
   *     given without a container
   */
  public SectionPosition {
    if (index < 0) {
      throw new IllegalArgumentException("index is negative");
    }
    if (parent != null && parent.isBlank()) {
      throw new IllegalArgumentException("parent is blank");
    }
    if (container != null && container.isBlank()) {
      throw new IllegalArgumentException("container is blank");
    }
    if (parent != null && container == null) {
      throw new IllegalArgumentException("a position within a section needs a container");
    }
  }

  /** A position on the top level of an entry or group. */
  public static SectionPosition topLevel(@Nullable String container, int index) {
    return new SectionPosition(null, container, index);
  }

  /** A position within a container of a section. */
  public static SectionPosition within(String parent, String container, int index) {
    Objects.requireNonNull(parent, "parent is null");
    Objects.requireNonNull(container, "container is null");
    return new SectionPosition(parent, container, index);
  }

  /** The parent section; empty for the top level. */
  public Optional<String> getParent() {
    return Optional.ofNullable(this.parent);
  }

  /** The container; empty for the fixed sections on the top level. */
  public Optional<String> getContainer() {
    return Optional.ofNullable(this.container);
  }
}
