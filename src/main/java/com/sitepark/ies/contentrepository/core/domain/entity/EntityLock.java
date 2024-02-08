package com.sitepark.ies.contentrepository.core.domain.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

public final class EntityLock implements Serializable {

  private final String entity;
  private final String user;
  private final OffsetDateTime created;
  private final OffsetDateTime lastAccess;
  private final long ttl;

  private static final long serialVersionUID = 1L;

  private EntityLock(Builder builder) {
    this.entity = builder.entity;
    this.user = builder.user;
    this.created = builder.created;
    this.lastAccess = builder.lastAccess;
    this.ttl = builder.ttl;
  }

  public String getEntity() {
    return entity;
  }

  public String getUser() {
    return user;
  }

  public OffsetDateTime getCreated() {
    return created;
  }

  public OffsetDateTime getLastAccess() {
    return lastAccess;
  }

  public long getTtl() {
    return ttl;
  }

  @Override
  public final int hashCode() {
    return Objects.hash(this.entity, this.user, this.created, this.lastAccess, this.ttl);
  }

  @Override
  public final boolean equals(Object o) {

    if (!(o instanceof EntityLock)) {
      return false;
    }

    EntityLock lock = (EntityLock) o;

    return Objects.equals(this.entity, lock.entity)
        && Objects.equals(this.user, lock.user)
        && Objects.equals(this.created, lock.created)
        && Objects.equals(this.lastAccess, lock.lastAccess)
        && Objects.equals(this.ttl, lock.ttl);
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public String toString() {
    StringBuilder b =
        new StringBuilder(100)
            .append("EntityLock[entity:")
            .append(this.entity)
            .append(", user:")
            .append(this.user)
            .append(", created:")
            .append(this.created)
            .append(", lastAccess:")
            .append(this.lastAccess)
            .append(", ttl:")
            .append(this.ttl)
            .append(']');
    return b.toString();
  }

  public static class Builder {

    private String entity;
    private String user;
    private OffsetDateTime created;
    private OffsetDateTime lastAccess;
    private long ttl;

    protected Builder() {}

    protected Builder(EntityLock entityLock) {
      this.entity = entityLock.entity;
      this.user = entityLock.user;
      this.created = entityLock.created;
      this.lastAccess = entityLock.lastAccess;
      this.ttl = entityLock.ttl;
    }

    public Builder entity(String entity) {
      Objects.requireNonNull(entity, "entity is null");
      if (!Identifier.isId(entity)) {
        throw new IllegalArgumentException(entity + " is not an id");
      }
      this.entity = entity;
      return this;
    }

    public Builder user(String user) {
      Objects.requireNonNull(user, "user is null");
      if (!Identifier.isId(user)) {
        throw new IllegalArgumentException(user + " is not an id");
      }
      this.user = user;
      return this;
    }

    public Builder created(OffsetDateTime created) {
      Objects.requireNonNull(created, "created is null");
      this.created = created;
      return this;
    }

    public Builder lastAccess(OffsetDateTime lastAccess) {
      Objects.requireNonNull(lastAccess, "lastAccess is null");
      this.lastAccess = lastAccess;
      return this;
    }

    public Builder ttl(long ttl) {
      if (ttl <= 0) {
        throw new IllegalArgumentException("ttl should be greater then 0");
      }
      this.ttl = ttl;
      return this;
    }

    public EntityLock build() {
      return new EntityLock(this);
    }
  }
}
