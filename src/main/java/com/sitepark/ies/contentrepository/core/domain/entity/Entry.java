package com.sitepark.ies.contentrepository.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An entry of the content repository: any kind of content, e.g. a page, a category, a
 * configuration, an event, a service, a person or the placement of a medium.
 *
 * <p>The entry type decides what an entry can do (e.g. whether it is published and creates a
 * file). The content is an ordered list of sections. An entry is immutable; the {@code with…}
 * methods return changed copies.
 */
@JsonDeserialize(builder = Entry.Builder.class)
public final class Entry extends Entity {

  private final String type;
  private final String filename;
  private final List<Section> sections;

  private Entry(Builder builder) {
    super(builder);
    this.type = builder.type;
    this.filename = builder.filename;
    this.sections = builder.sections;
  }

  /** The id of the entry type. */
  public String getType() {
    return this.type;
  }

  /** The file name used when the entry is published as a file. */
  public Optional<String> getFilename() {
    return Optional.ofNullable(this.filename);
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
  public Entry withSection(Section section, int index) {
    return this.toBuilder().sections(Sections.insert(this.sections, section, index)).build();
  }

  /** Returns a copy without the section. */
  public Entry withoutSection(String uuid) {
    return this.toBuilder().sections(Sections.remove(this.sections, uuid)).build();
  }

  /**
   * Returns a copy in which the section is moved.
   *
   * @param container the target container; {@code null} for the fixed sections
   * @param index the position within the target container
   */
  public Entry withSectionMovedTo(String uuid, String container, int index) {
    return this.toBuilder().sections(Sections.move(this.sections, uuid, container, index)).build();
  }

  /** Returns a copy in which the section has the given data. */
  public Entry withSectionData(String uuid, SectionData data) {
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
    return Objects.hash(super.hashCode(), this.type, this.filename, this.sections);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Entry that) || !that.canEqual(this)) {
      return false;
    }
    return super.equals(o)
        && Objects.equals(this.type, that.type)
        && Objects.equals(this.filename, that.filename)
        && Objects.equals(this.sections, that.sections);
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof Entry;
  }

  @Override
  protected String toStringFields() {
    return super.toStringFields()
        + ", type='"
        + this.type
        + '\''
        + ", filename='"
        + this.filename
        + '\''
        + ", sections="
        + this.sections;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder extends Entity.Builder<Builder> {

    private String type;
    private String filename;
    private List<Section> sections = List.of();

    private Builder() {
      super();
    }

    private Builder(Entry entry) {
      super(entry);
      this.type = entry.type;
      this.filename = entry.filename;
      this.sections = entry.sections;
    }

    public Builder type(String type) {
      Objects.requireNonNull(type, "type is null");
      if (type.isBlank()) {
        throw new IllegalArgumentException("type is blank");
      }
      this.type = type;
      return this;
    }

    public Builder filename(String filename) {
      Objects.requireNonNull(filename, "filename is null");
      if (filename.isBlank()) {
        throw new IllegalArgumentException("filename is blank");
      }
      this.filename = filename;
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
     * An entry is never a group.
     *
     * @throws IllegalArgumentException if {@code isGroup} is {@code true}
     */
    @Override
    @JsonProperty("group")
    @JsonAlias("isGroup")
    public Builder isGroup(boolean isGroup) {
      if (isGroup) {
        throw new IllegalArgumentException("an entry is not a group");
      }
      return super.isGroup(false);
    }

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Builds the entry.
     *
     * @throws IllegalStateException if the type is missing
     */
    @Override
    public Entry build() {
      if (this.type == null) {
        throw new IllegalStateException("type is not set");
      }
      return new Entry(this);
    }
  }
}
