package com.sitepark.ies.contentrepository.core.domain.entity;

import com.sitepark.ies.contentrepository.core.domain.exception.SectionNotFoundException;
import com.sitepark.ies.contentrepository.core.domain.value.SectionPosition;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.jspecify.annotations.Nullable;

/**
 * Operations on the tree of sections of an entry or a group. All operations return new,
 * unmodifiable lists; changed sections are rebuilt up to the top level.
 *
 * <p>On the top level, positions are counted within a container: index 0 is the first section of
 * that container. Sections without a container (fixed sections) form a container of their own.
 * Within a section, the index is the position in the list of the named container.
 */
@SuppressWarnings("PMD.TooManyMethods") // one small method per step of the tree operations
final class Sections {

  private Sections() {}

  /**
   * Returns an unmodifiable copy of the top-level sections.
   *
   * @throws IllegalArgumentException if two sections anywhere in the tree have the same UUID
   */
  static List<Section> copyOf(List<Section> sections) {
    Objects.requireNonNull(sections, "sections is null");
    Set<String> uuids = new HashSet<>();
    for (Section section : sections) {
      Objects.requireNonNull(section, "section is null");
      addUuids(uuids, section);
    }
    return List.copyOf(sections);
  }

  /** Finds a section anywhere in the tree. */
  static Optional<Section> find(List<Section> sections, String uuid) {
    Objects.requireNonNull(uuid, "uuid is null");
    return sections.stream()
        .flatMap(Section::stream)
        .filter(s -> s.getUuid().equals(uuid))
        .findFirst();
  }

  /**
   * Inserts a section on the top level at a position within its container.
   *
   * @param index the position within the container of the section, from 0 to the number of sections
   *     in that container
   * @throws IllegalArgumentException if a section with the same UUID exists
   * @throws IndexOutOfBoundsException if the index is outside the container
   */
  static List<Section> insert(List<Section> sections, Section section, int index) {
    Objects.requireNonNull(section, "section is null");
    return insert(
        sections, section, SectionPosition.topLevel(section.getContainer().orElse(null), index));
  }

  /**
   * Inserts a section at a position in the tree. The section gets the container of the position.
   *
   * @throws IllegalArgumentException if a section of the inserted subtree exists already
   * @throws SectionNotFoundException if the parent of the position does not exist
   * @throws IndexOutOfBoundsException if the index is outside the container
   */
  static List<Section> insert(List<Section> sections, Section section, SectionPosition position) {
    Objects.requireNonNull(section, "section is null");
    Objects.requireNonNull(position, "position is null");
    Set<String> uuids = new HashSet<>();
    sections.forEach(s -> addUuids(uuids, s));
    addUuids(uuids, section);
    Section placed = section.toBuilder().container(position.container()).build();
    if (position.parent() == null) {
      List<Section> result = new ArrayList<>(sections);
      result.add(topLevelIndex(result, position.container(), position.index()), placed);
      return List.copyOf(result);
    }
    // a position within a section always names a container (see SectionPosition)
    String container = Objects.requireNonNull(position.container(), "container is null");
    return update(
        sections,
        position.parent(),
        parent -> {
          List<Section> children = new ArrayList<>(parent.getChildren(container));
          if (position.index() > children.size()) {
            throw new IndexOutOfBoundsException(
                "index "
                    + position.index()
                    + " outside of container "
                    + container
                    + " with "
                    + children.size());
          }
          children.add(position.index(), placed);
          return parent.toBuilder().children(container, children).build();
        });
  }

  /**
   * Removes a section together with the sections below it.
   *
   * @throws SectionNotFoundException if there is no section with the UUID
   */
  static List<Section> remove(List<Section> sections, String uuid) {
    Objects.requireNonNull(uuid, "uuid is null");
    return removeFrom(sections, uuid).orElseThrow(() -> new SectionNotFoundException(uuid));
  }

  /**
   * Moves a section on the top level to a position within a container; the container may differ
   * from the current one.
   *
   * @param container the target container; {@code null} for the fixed sections
   * @param index the position within the target container, counted without the moved section
   * @throws SectionNotFoundException if there is no section with the UUID
   * @throws IndexOutOfBoundsException if the index is outside the target container
   */
  static List<Section> move(
      List<Section> sections, String uuid, @Nullable String container, int index) {
    return move(sections, uuid, SectionPosition.topLevel(container, index));
  }

  /**
   * Moves a section together with the sections below it to a position in the tree.
   *
   * @param position the target, with the index counted without the moved section
   * @throws SectionNotFoundException if the section or the target parent does not exist
   * @throws IllegalArgumentException if the target parent is the section itself or below it
   * @throws IndexOutOfBoundsException if the index is outside the target container
   */
  static List<Section> move(List<Section> sections, String uuid, SectionPosition position) {
    Objects.requireNonNull(position, "position is null");
    Section section = find(sections, uuid).orElseThrow(() -> new SectionNotFoundException(uuid));
    if (position.parent() != null
        && section.stream().anyMatch(s -> s.getUuid().equals(position.parent()))) {
      throw new IllegalArgumentException(
          "section " + uuid + " cannot be moved into itself or below itself");
    }
    return insert(remove(sections, uuid), section, position);
  }

  /**
   * Replaces the data of a section; type, container, position and children stay.
   *
   * @throws SectionNotFoundException if there is no section with the UUID
   */
  static List<Section> replaceData(List<Section> sections, String uuid, SectionData data) {
    Objects.requireNonNull(data, "data is null");
    return update(sections, uuid, section -> section.toBuilder().data(data).build());
  }

  /**
   * Replaces a section anywhere in the tree by the result of the change.
   *
   * @throws SectionNotFoundException if there is no section with the UUID
   */
  private static List<Section> update(
      List<Section> sections, String uuid, UnaryOperator<Section> change) {
    return updateIn(sections, uuid, change).orElseThrow(() -> new SectionNotFoundException(uuid));
  }

  /** The changed list, or empty if the section is not in this part of the tree. */
  private static Optional<List<Section>> updateIn(
      List<Section> sections, String uuid, UnaryOperator<Section> change) {
    for (int i = 0; i < sections.size(); i++) {
      Section section = sections.get(i);
      Optional<Section> changed =
          section.getUuid().equals(uuid)
              ? Optional.of(change.apply(section))
              : updateChildren(section, list -> updateIn(list, uuid, change));
      if (changed.isPresent()) {
        return Optional.of(replaced(sections, i, changed.get()));
      }
    }
    return Optional.empty();
  }

  /** The list without the section, or empty if it is not in this part of the tree. */
  private static Optional<List<Section>> removeFrom(List<Section> sections, String uuid) {
    for (int i = 0; i < sections.size(); i++) {
      Section section = sections.get(i);
      if (section.getUuid().equals(uuid)) {
        return Optional.of(removed(sections, i));
      }
      Optional<Section> changed = updateChildren(section, list -> removeFrom(list, uuid));
      if (changed.isPresent()) {
        return Optional.of(replaced(sections, i, changed.get()));
      }
    }
    return Optional.empty();
  }

  private static List<Section> removed(List<Section> sections, int index) {
    List<Section> result = new ArrayList<>(sections);
    result.remove(index);
    return List.copyOf(result);
  }

  private static List<Section> replaced(List<Section> sections, int index, Section section) {
    List<Section> result = new ArrayList<>(sections);
    result.set(index, section);
    return List.copyOf(result);
  }

  /**
   * Applies a change to the containers of a section; the change answers empty for a container it
   * leaves untouched.
   *
   * @return the changed section, or empty if no container changed
   */
  private static Optional<Section> updateChildren(
      Section section, Function<List<Section>, Optional<List<Section>>> change) {
    for (Map.Entry<String, List<Section>> container : section.getContainers().entrySet()) {
      Optional<List<Section>> changed = change.apply(container.getValue());
      if (changed.isPresent()) {
        return Optional.of(section.toBuilder().children(container.getKey(), changed.get()).build());
      }
    }
    return Optional.empty();
  }

  private static void addUuids(Set<String> uuids, Section section) {
    section.stream()
        .forEach(
            s -> {
              if (!uuids.add(s.getUuid())) {
                throw new IllegalArgumentException("duplicate section uuid " + s.getUuid());
              }
            });
  }

  /**
   * The index in the whole top-level list for a position within a container. A position after the
   * last section of the container is placed directly behind it; a container without sections is
   * appended at the end.
   */
  private static int topLevelIndex(List<Section> sections, @Nullable String container, int index) {
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
