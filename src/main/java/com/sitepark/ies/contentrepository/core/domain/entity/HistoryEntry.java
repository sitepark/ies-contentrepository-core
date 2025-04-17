package com.sitepark.ies.contentrepository.core.domain.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public final class HistoryEntry implements Serializable {

  private final String entityId;
  private final String initiator;
  private final String userId;
  private final long timestamp;
  private final HistoryEntryType type;
  private final String comment;

  @Serial private static final long serialVersionUID = 1L;

  private HistoryEntry(Builder builder) {
    this.entityId = builder.entityId;
    this.initiator = builder.initiator;
    this.userId = builder.userId;
    this.timestamp = builder.timestamp;
    this.type = builder.type;
    this.comment = builder.comment;
  }

  public String getEntityId() {
    return entityId;
  }

  /**
   * The initiator can be a user of a system-service or other subjects who could authenticate
   * themselves to work with the system.
   */
  public String getInitiator() {
    return initiator;
  }

  /** If the initiator is a user of the IES, this returns the ID of the user. */
  public Optional<String> getUserId() {
    return Optional.ofNullable(this.userId);
  }

  public long getTimestamp() {
    return timestamp;
  }

  public HistoryEntryType getType() {
    return type;
  }

  public String getComment() {
    return comment;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.entityId, this.initiator, this.userId, this.timestamp, this.type, this.comment);
  }

  @Override
  public boolean equals(Object o) {

    if (!(o instanceof HistoryEntry that)) {
      return false;
    }

    return Objects.equals(this.entityId, that.entityId)
        && Objects.equals(this.initiator, that.initiator)
        && Objects.equals(this.userId, that.userId)
        && Objects.equals(this.timestamp, that.timestamp)
        && Objects.equals(this.type, that.type)
        && Objects.equals(this.comment, that.comment);
  }

  @Override
  public String toString() {
    return "HistoryEntry{"
        + "entityId="
        + entityId
        + ", initiator='"
        + initiator
        + '\''
        + ", userId="
        + userId
        + ", timestamp="
        + timestamp
        + ", type="
        + type
        + ", comment='"
        + comment
        + '\''
        + '}';
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  public static final class Builder {

    private String entityId;
    private String initiator;
    private String userId;
    private long timestamp;
    private HistoryEntryType type;
    private String comment;

    private Builder() {}

    private Builder(HistoryEntry historyEntry) {
      this.entityId = historyEntry.entityId;
      this.initiator = historyEntry.initiator;
      this.userId = historyEntry.userId;
      this.timestamp = historyEntry.timestamp;
      this.type = historyEntry.type;
      this.comment = historyEntry.comment;
    }

    public Builder entityId(String entityId) {
      this.entityId = entityId;
      return this;
    }

    public Builder initiator(String initiator) {
      Objects.requireNonNull(initiator, "initiator is null");
      this.initiator = initiator;
      return this;
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder timestamp(long timestamp) {
      if (timestamp <= 0) {
        throw new IllegalArgumentException("timestamp must be greater than 0");
      }
      this.timestamp = timestamp;
      return this;
    }

    public Builder type(HistoryEntryType type) {
      Objects.requireNonNull(type, "type is null");
      this.type = type;
      return this;
    }

    public Builder comment(String comment) {
      Objects.requireNonNull(comment, "comment is null");
      this.comment = comment;
      return this;
    }

    public HistoryEntry build() {
      return new HistoryEntry(this);
    }
  }
}
