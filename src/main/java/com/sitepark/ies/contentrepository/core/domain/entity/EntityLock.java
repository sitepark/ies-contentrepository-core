package com.sitepark.ies.contentrepository.core.domain.entity;

import com.sitepark.ies.sharedkernel.base.Identifier;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

public final class EntityLock implements Serializable {

  private final String entityId;
  private final String userId;
  private final OffsetDateTime created;
  private final OffsetDateTime lastAccess;
  private final long ttl;

  @Serial private static final long serialVersionUID = 1L;

  private EntityLock(Builder builder) {
    this.entityId = builder.entityId;
    this.userId = builder.userId;
    this.created = builder.created;
    this.lastAccess = builder.lastAccess;
    this.ttl = builder.ttl;
  }

  public String getEntityId() {
    return entityId;
  }

  public String getUserId() {
    return userId;
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
  public int hashCode() {
    return Objects.hash(this.entityId, this.userId, this.created, this.lastAccess, this.ttl);
  }

  @Override
  public boolean equals(Object o) {

    if (!(o instanceof EntityLock lock)) {
      return false;
    }

    return Objects.equals(this.entityId, lock.entityId)
        && Objects.equals(this.userId, lock.userId)
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
    return "EntityLock{"
        + "entityId='"
        + entityId
        + '\''
        + ", userId='"
        + userId
        + '\''
        + ", created="
        + created
        + ", lastAccess="
        + lastAccess
        + ", ttl="
        + ttl
        + '}';
  }

  public static class Builder {

    private String entityId;
    private String userId;
    private OffsetDateTime created;
    private OffsetDateTime lastAccess;
    private long ttl;

    protected Builder() {}

    protected Builder(EntityLock entityLock) {
      this.entityId = entityLock.entityId;
      this.userId = entityLock.userId;
      this.created = entityLock.created;
      this.lastAccess = entityLock.lastAccess;
      this.ttl = entityLock.ttl;
    }

    public Builder entityId(String entityId) {
      Objects.requireNonNull(entityId, "entityId is null");
      if (!Identifier.isId(entityId)) {
        throw new IllegalArgumentException(entityId + " is not an id");
      }
      this.entityId = entityId;
      return this;
    }

    public Builder userId(String userId) {
      Objects.requireNonNull(userId, "userId is null");
      if (!Identifier.isId(userId)) {
        throw new IllegalArgumentException(userId + " is not an id");
      }
      this.userId = userId;
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
