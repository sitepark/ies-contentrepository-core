package com.sitepark.ies.contentrepository.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

/**
 * A group of the content repository: it structures entries and groups hierarchically.
 *
 * <p>A group can carry content in sections like an entry, but it is neither published nor
 * aggregated. A group is immutable; the {@code with…} methods return changed copies.
 */
@JsonDeserialize(builder = Group.Builder.class)
public final class Group extends Entity {

  private final @Nullable String type;
  private final @Nullable String directory;
  private final List<Section> sections;

  private Group(Builder builder) {
    super(builder);
    this.type = builder.type;
    this.directory = builder.directory;
    this.sections = builder.sections;
  }

  /** The id of the group type, which may define an input mask for the content of the group. */
  public Optional<String> getType() {
    return Optional.ofNullable(this.type);
  }

  /** The directory used for the files of the entries within the group. */
  public Optional<String> getDirectory() {
    return Optional.ofNullable(this.directory);
  }

  /** The sections in their order. */
  public List<Section> getSections() {
    return this.sections;
  }

  public Optional<Section> getSection(String uuid) {
    return Sections.find(this.sections, uuid);
  }

  /**
   * Returns a copy with an additional section.
   *
   * @param index the position within the container of the section
   */
  public Group withSection(Section section, int index) {
    return this.toBuilder().sections(Sections.insert(this.sections, section, index)).build();
  }

  /** Returns a copy without the section. */
  public Group withoutSection(String uuid) {
    return this.toBuilder().sections(Sections.remove(this.sections, uuid)).build();
  }

  /**
   * Returns a copy in which the section is moved.
   *
   * @param container the target container; {@code null} for the fixed sections
   * @param index the position within the target container
   */
  public Group withSectionMovedTo(String uuid, @Nullable String container, int index) {
    return this.toBuilder().sections(Sections.move(this.sections, uuid, container, index)).build();
  }

  /** Returns a copy in which the section has the given data. */
  public Group withSectionData(String uuid, SectionData data) {
    return this.toBuilder().sections(Sections.replaceData(this.sections, uuid, data)).build();
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.type, this.directory, this.sections);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Group that) || !that.canEqual(this)) {
      return false;
    }
    return super.equals(o)
        && Objects.equals(this.type, that.type)
        && Objects.equals(this.directory, that.directory)
        && Objects.equals(this.sections, that.sections);
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof Group;
  }

  @Override
  protected String toStringFields() {
    return super.toStringFields()
        + ", type='"
        + this.type
        + '\''
        + ", directory='"
        + this.directory
        + '\''
        + ", sections="
        + this.sections;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder extends Entity.Builder<Builder> {

    private @Nullable String type;
    private @Nullable String directory;
    private List<Section> sections = List.of();

    private Builder() {
      super();
      super.isGroup(true);
    }

    private Builder(Group group) {
      super(group);
      this.type = group.type;
      this.directory = group.directory;
      this.sections = group.sections;
    }

    public Builder type(String type) {
      Objects.requireNonNull(type, "type is null");
      if (type.isBlank()) {
        throw new IllegalArgumentException("type is blank");
      }
      this.type = type;
      return this;
    }

    public Builder directory(String directory) {
      Objects.requireNonNull(directory, "directory is null");
      if (directory.isBlank()) {
        throw new IllegalArgumentException("directory is blank");
      }
      this.directory = directory;
      return this;
    }

    /**
     * Sets the sections.
     *
     * @throws IllegalArgumentException if two sections have the same UUID
     */
    public Builder sections(List<Section> sections) {
      this.sections = Sections.copyOf(sections);
      return this;
    }

    /**
     * A group is always a group.
     *
     * @throws IllegalArgumentException if {@code isGroup} is {@code false}
     */
    @Override
    @JsonProperty("group")
    @JsonAlias("isGroup")
    public Builder isGroup(boolean isGroup) {
      if (!isGroup) {
        throw new IllegalArgumentException("a group is always a group");
      }
      return super.isGroup(true);
    }

    @Override
    protected Builder self() {
      return this;
    }

    @Override
    public Group build() {
      return new Group(this);
    }
  }
}
