package com.sitepark.ies.contentrepository.core.domain.entity;

import com.sitepark.ies.contentrepository.core.domain.exception.SectionNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.Nullable;

/**
 * Operations on the ordered sections of an entry or a group. All operations return new, unmodifiable
 * lists.
 *
 * <p>Positions are counted within a container: index 0 is the first section of that container.
 * Sections without a container (fixed sections) form a container of their own.
 */
final class Sections {

  private Sections() {}

  /**
   * Returns an unmodifiable copy of the sections.
   *
   * @throws IllegalArgumentException if two sections have the same UUID
   */
  static List<Section> copyOf(List<Section> sections) {
    Objects.requireNonNull(sections, "sections is null");
    Set<String> uuids = new HashSet<>();
    for (Section section : sections) {
      Objects.requireNonNull(section, "section is null");
      if (!uuids.add(section.getUuid())) {
        throw new IllegalArgumentException("duplicate section uuid " + section.getUuid());
      }
    }
    return List.copyOf(sections);
  }

  static Optional<Section> find(List<Section> sections, String uuid) {
    Objects.requireNonNull(uuid, "uuid is null");
    return sections.stream().filter(s -> s.getUuid().equals(uuid)).findFirst();
  }

  /**
   * Inserts a section at a position within its container.
   *
   * @param index the position within the container of the section, from 0 to the number of sections
   *     in that container
   * @throws IllegalArgumentException if a section with the same UUID exists
   * @throws IndexOutOfBoundsException if the index is outside the container
   */
  static List<Section> insert(List<Section> sections, Section section, int index) {
    Objects.requireNonNull(section, "section is null");
    if (find(sections, section.getUuid()).isPresent()) {
      throw new IllegalArgumentException("duplicate section uuid " + section.getUuid());
    }
    List<Section> result = new ArrayList<>(sections);
    result.add(listIndex(result, section.getContainer().orElse(null), index), section);
    return List.copyOf(result);
  }

  /**
   * Removes a section.
   *
   * @throws SectionNotFoundException if there is no section with the UUID
   */
  static List<Section> remove(List<Section> sections, String uuid) {
    Section section = find(sections, uuid).orElseThrow(() -> new SectionNotFoundException(uuid));
    List<Section> result = new ArrayList<>(sections);
    result.remove(section);
    return List.copyOf(result);
  }

  /**
   * Moves a section to a position within a container; the container may differ from the current
   * one.
   *
   * @param container the target container; {@code null} for the fixed sections
   * @param index the position within the target container, counted without the moved section
   * @throws SectionNotFoundException if there is no section with the UUID
   * @throws IndexOutOfBoundsException if the index is outside the target container
   */
  static List<Section> move(
      List<Section> sections, String uuid, @Nullable String container, int index) {
    Section section = find(sections, uuid).orElseThrow(() -> new SectionNotFoundException(uuid));
    List<Section> result = new ArrayList<>(sections);
    result.remove(section);
    Section moved = section.toBuilder().container(container).build();
    result.add(listIndex(result, container, index), moved);
    return List.copyOf(result);
  }

  /**
   * Replaces the data of a section; type, container and position stay.
   *
   * @throws SectionNotFoundException if there is no section with the UUID
   */
  static List<Section> replaceData(List<Section> sections, String uuid, SectionData data) {
    Objects.requireNonNull(data, "data is null");
    Section section = find(sections, uuid).orElseThrow(() -> new SectionNotFoundException(uuid));
    List<Section> result = new ArrayList<>(sections);
    result.set(result.indexOf(section), section.toBuilder().data(data).build());
    return List.copyOf(result);
  }

  /**
   * The index in the whole list for a position within a container. A position after the last
   * section of the container is placed directly behind it; a container without sections is
   * appended at the end.
   */
  private static int listIndex(List<Section> sections, @Nullable String container, int index) {
    int position = 0;
    int afterLast = -1;
    for (int i = 0; i < sections.size(); i++) {
      if (Objects.equals(sections.get(i).getContainer().orElse(null), container)) {
        if (position == index) {
          return i;
        }
        position++;
        afterLast = i + 1;
      }
    }
    if (index != position) {
      throw new IndexOutOfBoundsException(
          "index " + index + " outside of container " + container + " with " + position);
    }
    return afterLast < 0 ? sections.size() : afterLast;
  }
}
