package com.sitepark.ies.contentrepository.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.sitepark.ies.sharedkernel.anchor.Anchor;
import com.sitepark.ies.sharedkernel.base.Identifier;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

/**
 * The master data shared by all objects of the content repository: entries and groups.
 *
 * <p>Subclasses add their own fields and override {@link #canEqual(Object)}, so an entity is never
 * equal to an entry or a group with the same master data.
 */
@JsonDeserialize(builder = Entity.EntityBuilder.class)
public class Entity {

  private final @Nullable String id;
  private final @Nullable Anchor anchor;
  private final @Nullable String name;
  private final @Nullable String parent;
  private final @Nullable Instant version;
  private final @Nullable Instant created;
  private final @Nullable String createdBy;
  private final @Nullable Instant changed;
  private final @Nullable String changedBy;
  private final boolean group;

  protected Entity(Builder<?> builder) {
    this.id = builder.id;
    this.anchor = builder.anchor;
    this.name = builder.name;
    this.parent = builder.parent;
    this.version = builder.version;
    this.created = builder.created;
    this.createdBy = builder.createdBy;
    this.changed = builder.changed;
    this.changedBy = builder.changedBy;
    this.group = builder.group;
  }

  public Optional<String> getId() {
    return Optional.ofNullable(this.id);
  }

  public Optional<Anchor> getAnchor() {
    return Optional.ofNullable(this.anchor);
  }

  public Optional<String> getName() {
    return Optional.ofNullable(this.name);
  }

  public Optional<String> getParent() {
    return Optional.ofNullable(this.parent);
  }

  /** The version of the object; every change creates a new version of the whole object. */
  public Optional<Instant> getVersion() {
    return Optional.ofNullable(this.version);
  }

  public Optional<Instant> getCreated() {
    return Optional.ofNullable(this.created);
  }

  /** The id of the user who created the object. */
  public Optional<String> getCreatedBy() {
    return Optional.ofNullable(this.createdBy);
  }

  public Optional<Instant> getChanged() {
    return Optional.ofNullable(this.changed);
  }

  /** The id of the user who changed the object last. */
  public Optional<String> getChangedBy() {
    return Optional.ofNullable(this.changedBy);
  }

  public boolean isGroup() {
    return this.group;
  }

  public static Builder<?> builder() {
    return new EntityBuilder();
  }

  public Builder<?> toBuilder() {
    return new EntityBuilder(this);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.id,
        this.anchor,
        this.name,
        this.parent,
        this.version,
        this.created,
        this.createdBy,
        this.changed,
        this.changedBy,
        this.group);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Entity that) || !that.canEqual(this)) {
      return false;
    }
    return Objects.equals(this.id, that.id)
        && Objects.equals(this.anchor, that.anchor)
        && Objects.equals(this.name, that.name)
        && Objects.equals(this.parent, that.parent)
        && Objects.equals(this.version, that.version)
        && Objects.equals(this.created, that.created)
        && Objects.equals(this.createdBy, that.createdBy)
        && Objects.equals(this.changed, that.changed)
        && Objects.equals(this.changedBy, that.changedBy)
        && this.group == that.group;
  }

  /**
   * Whether the given object may be equal to this one. Subclasses with own fields override it, so
   * that an entity is never equal to an entry or a group with the same master data.
   */
  protected boolean canEqual(Object other) {
    return other instanceof Entity;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "{" + this.toStringFields() + "}";
  }

  /** The fields for {@link #toString()}; subclasses append their own. */
  protected String toStringFields() {
    return "id='"
        + this.id
        + '\''
        + ", anchor="
        + this.anchor
        + ", name='"
        + this.name
        + '\''
        + ", parent='"
        + this.parent
        + '\''
        + ", version="
        + this.version
        + ", created="
        + this.created
        + ", createdBy='"
        + this.createdBy
        + '\''
        + ", changed="
        + this.changed
        + ", changedBy='"
        + this.changedBy
        + '\''
        + ", group="
        + this.group;
  }

  @SuppressWarnings("PMD.TooManyMethods")
  public abstract static class Builder<B extends Builder<B>> {

    private @Nullable String id;
    private @Nullable Anchor anchor;
    private @Nullable String name;
    private @Nullable String parent;
    private @Nullable Instant version;
    private @Nullable Instant created;
    private @Nullable String createdBy;
    private @Nullable Instant changed;
    private @Nullable String changedBy;
    private boolean group;

    protected Builder() {}

    protected Builder(Entity entity) {
      this.id = entity.id;
      this.anchor = entity.anchor;
      this.name = entity.name;
      this.parent = entity.parent;
      this.version = entity.version;
      this.created = entity.created;
      this.createdBy = entity.createdBy;
      this.changed = entity.changed;
      this.changedBy = entity.changedBy;
      this.group = entity.group;
    }

    public B id(String id) {
      this.id = requireId(id, "id");
      return this.self();
    }

    public B anchor(String anchor) {
      Objects.requireNonNull(anchor, "anchor is null");
      this.anchor = Anchor.ofString(anchor);
      return this.self();
    }

    public B anchor(Anchor anchor) {
      Objects.requireNonNull(anchor, "anchor is null");
      this.anchor = anchor;
      return this.self();
    }

    public B name(String name) {
      Objects.requireNonNull(name, "name is null");
      if (name.isBlank()) {
        throw new IllegalArgumentException("name is blank");
      }
      this.name = name;
      return this.self();
    }

    public B parent(String parent) {
      this.parent = requireId(parent, "parent");
      return this.self();
    }

    public B version(Instant version) {
      Objects.requireNonNull(version, "version is null");
      this.version = version;
      return this.self();
    }

    public B created(Instant created) {
      Objects.requireNonNull(created, "created is null");
      this.created = created;
      return this.self();
    }

    public B createdBy(String createdBy) {
      this.createdBy = requireId(createdBy, "createdBy");
      return this.self();
    }

    public B changed(Instant changed) {
      Objects.requireNonNull(changed, "changed is null");
      this.changed = changed;
      return this.self();
    }

    public B changedBy(String changedBy) {
      this.changedBy = requireId(changedBy, "changedBy");
      return this.self();
    }

    @JsonProperty("group")
    @JsonAlias("isGroup")
    public B isGroup(boolean isGroup) {
      this.group = isGroup;
      return this.self();
    }

    protected abstract B self();

    public Entity build() {
      return new Entity(this);
    }

    private static String requireId(String value, String name) {
      Objects.requireNonNull(value, name + " is null");
      if (!Identifier.isId(value)) {
        throw new IllegalArgumentException(value + " is not an id");
      }
      return value;
    }
  }

  @JsonPOJOBuilder(withPrefix = "")
  protected static class EntityBuilder extends Builder<EntityBuilder> {
    protected EntityBuilder() {}

    protected EntityBuilder(Entity entity) {
      super(entity);
    }

    @Override
    protected EntityBuilder self() {
      return this;
    }
  }
}
