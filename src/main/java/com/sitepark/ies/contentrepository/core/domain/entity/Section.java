package com.sitepark.ies.contentrepository.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * A section of an entry or a group: a typed block of content.
 *
 * <p>A section is identified by its UUID, which stays the same when the section is moved. Its type
 * names a section type, which may define a JSON schema for the data and a grant needed to edit the
 * section. Sections that belong to a container (e.g. {@code sp_main_iterate}) can be added, moved
 * and removed by editors; sections without a container are fixed parts of the entry.
 *
 * <p>A section can hold other sections in named containers, e.g. a section of a page holds the
 * sections of its {@code items}. Each child names the container it lies in.
 */
@JsonDeserialize(builder = Section.Builder.class)
public final class Section {

  private final String uuid;
  private final String type;
  private final String container;
  private final SectionData data;
  private final Map<String, List<Section>> containers;

  private Section(Builder builder) {
    this.uuid = builder.uuid;
    this.type = builder.type;
    this.container = builder.container;
    this.data = builder.data;
    this.containers = Collections.unmodifiableMap(new LinkedHashMap<>(builder.containers));
  }

  public String getUuid() {
    return this.uuid;
  }

  /** The id of the section type. */
  public String getType() {
    return this.type;
  }

  /** The container the section belongs to; empty for a fixed section. */
  public Optional<String> getContainer() {
    return Optional.ofNullable(this.container);
  }

  public SectionData getData() {
    return this.data;
  }

  /** The child sections by the name of their container, each in its order. */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public Map<String, List<Section>> getContainers() {
    return this.containers;
  }

  /** This section and all sections below it, in tree order (each parent before its children). */
  public Stream<Section> stream() {
    return Stream.concat(
        Stream.of(this),
        this.containers.values().stream().flatMap(List::stream).flatMap(Section::stream));
  }

  /** The child sections in a container; empty if the container holds none. */
  public List<Section> getChildren(String container) {
    Objects.requireNonNull(container, "container is null");
    return this.containers.getOrDefault(container, List.of());
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.uuid, this.type, this.container, this.data, this.containers);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Section that
        && Objects.equals(this.uuid, that.uuid)
        && Objects.equals(this.type, that.type)
        && Objects.equals(this.container, that.container)
        && Objects.equals(this.data, that.data)
        && Objects.equals(this.containers, that.containers);
  }

  @Override
  public String toString() {
    return "Section{"
        + "uuid='"
        + this.uuid
        + '\''
        + ", type='"
        + this.type
        + '\''
        + ", container='"
        + this.container
        + '\''
        + ", data="
        + this.data
        + ", containers="
        + this.containers
        + '}';
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {

    private String uuid;
    private String type;
    private String container;
    private SectionData data = SectionData.empty();

    @SuppressWarnings("PMD.UseConcurrentHashMap") // a builder is not shared between threads
    private final Map<String, List<Section>> containers = new LinkedHashMap<>();

    private Builder() {}

    private Builder(Section section) {
      this.uuid = section.uuid;
      this.type = section.type;
      this.container = section.container;
      this.data = section.data;
      this.containers.putAll(section.containers);
    }

    /**
     * Sets the UUID of the section.
     *
     * @param uuid a UUID in its canonical string form
     * @throws IllegalArgumentException if {@code uuid} is not a UUID
     */
    public Builder uuid(String uuid) {
      Objects.requireNonNull(uuid, "uuid is null");
      try {
        this.uuid = UUID.fromString(uuid).toString();
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(uuid + " is not a uuid", e);
      }
      return this;
    }

    public Builder type(String type) {
      Objects.requireNonNull(type, "type is null");
      if (type.isBlank()) {
        throw new IllegalArgumentException("type is blank");
      }
      this.type = type;
      return this;
    }

    public Builder container(String container) {
      if (container != null && container.isBlank()) {
        throw new IllegalArgumentException("container is blank");
      }
      this.container = container;
      return this;
    }

    public Builder data(SectionData data) {
      Objects.requireNonNull(data, "data is null");
      this.data = data;
      return this;
    }

    /**
     * Sets all child sections, replacing the existing ones.
     *
     * @throws IllegalArgumentException if a child does not name the container it lies in
     */
    public Builder containers(Map<String, List<Section>> containers) {
      Objects.requireNonNull(containers, "containers is null");
      this.containers.clear();
      containers.forEach(this::children);
      return this;
    }

    /**
     * Sets the child sections of a container; an empty list removes the container.
     *
     * @throws IllegalArgumentException if a child does not name this container
     */
    public Builder children(String container, List<Section> children) {
      Objects.requireNonNull(container, "container is null");
      Objects.requireNonNull(children, "children is null");
      if (container.isBlank()) {
        throw new IllegalArgumentException("container is blank");
      }
      for (Section child : children) {
        Objects.requireNonNull(child, "child is null");
        if (!container.equals(child.container)) {
          throw new IllegalArgumentException(
              "section "
                  + child.uuid
                  + " lies in container "
                  + container
                  + " but names "
                  + child.container);
        }
      }
      if (children.isEmpty()) {
        this.containers.remove(container);
      } else {
        this.containers.put(container, List.copyOf(children));
      }
      return this;
    }

    /**
     * Builds the section.
     *
     * @throws IllegalStateException if the uuid or the type is missing
     */
    public Section build() {
      if (this.uuid == null) {
        throw new IllegalStateException("uuid is not set");
      }
      if (this.type == null) {
        throw new IllegalStateException("type is not set");
      }
      return new Section(this);
    }
  }
}
