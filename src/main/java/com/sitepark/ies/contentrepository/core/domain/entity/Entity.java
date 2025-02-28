package com.sitepark.ies.contentrepository.core.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

@JsonDeserialize(builder = Entity.EntityBuilder.class)
public class Entity {

  private final String id;
  private final Anchor anchor;
  private final String name;
  private final String parent;
  private final OffsetDateTime version;
  private final boolean group;

  protected Entity(Builder<?> builder) {
    this.id = builder.id;
    this.anchor = builder.anchor;
    this.name = builder.name;
    this.parent = builder.parent;
    this.version = builder.version;
    this.group = builder.group;
  }

  public Optional<String> getId() {
    if (this.id == null) {
      return Optional.empty();
    } else {
      return Optional.of(this.id);
    }
  }

  public Optional<Anchor> getAnchor() {
    return Optional.ofNullable(this.anchor);
  }

  public Optional<String> getName() {
    return Optional.ofNullable(this.name);
  }

  public Optional<String> getParent() {
    if (this.parent == null) {
      return Optional.empty();
    } else {
      return Optional.of(this.parent);
    }
  }

  public Optional<OffsetDateTime> getVersion() {
    if (this.version == null) {
      return Optional.empty();
    } else {
      return Optional.of(this.version);
    }
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
  public final int hashCode() {

    return Objects.hash(this.id, this.anchor, this.name, this.parent, this.version, this.group);
  }

  @Override
  public final boolean equals(Object o) {

    if (!(o instanceof Entity that)) {
      return false;
    }

    return Objects.equals(this.id, that.id)
        && Objects.equals(this.anchor, that.anchor)
        && Objects.equals(this.name, that.name)
        && Objects.equals(this.parent, that.parent)
        && Objects.equals(this.version, that.version)
        && Objects.equals(this.group, that.group);
  }

  @Override
  public String toString() {
    return "Entity{"
        + "id='"
        + id
        + '\''
        + ", anchor="
        + anchor
        + ", name='"
        + name
        + '\''
        + ", parent='"
        + parent
        + '\''
        + ", version="
        + version
        + ", group="
        + group
        + '}';
  }

  public abstract static class Builder<B extends Builder<B>> {

    private String id;
    private Anchor anchor;
    private String name;
    private String parent;
    private OffsetDateTime version;
    private boolean group;

    protected Builder() {}

    protected Builder(Entity entity) {
      this.id = entity.id;
      this.anchor = entity.anchor;
      this.name = entity.name;
      this.parent = entity.parent;
      this.version = entity.version;
      this.group = entity.group;
    }

    public B id(String id) {
      Objects.requireNonNull(id, "id is null");
      if (!Identifier.isId(id)) {
        throw new IllegalArgumentException(id + " is not an id");
      }
      this.id = id;
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
      Objects.requireNonNull(parent, "parent is null");
      if (!Identifier.isId(parent)) {
        throw new IllegalArgumentException(parent + " is not an id");
      }
      this.parent = parent;
      return this.self();
    }

    public B version(OffsetDateTime version) {
      Objects.requireNonNull(version, "version is null");
      this.version = version;
      return this.self();
    }

    public B isGroup(boolean isGroup) {
      this.group = isGroup;
      return this.self();
    }

    protected abstract B self();

    public Entity build() {
      return new Entity(this);
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
