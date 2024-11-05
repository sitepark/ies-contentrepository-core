package com.sitepark.ies.contentrepository.core.domain.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public final class HistoryEntry implements Serializable {

  private final long entity;
  private final String initiator;
  private final Long user;
  private final long timestamp;
  private final HistoryEntryType type;
  private final String comment;

  private static final long serialVersionUID = 1L;

  private HistoryEntry(Builder builder) {
    this.entity = builder.entity;
    this.initiator = builder.initiator;
    this.user = builder.user;
    this.timestamp = builder.timestamp;
    this.type = builder.type;
    this.comment = builder.comment;
  }

  public long getEntity() {
    return entity;
  }

  /**
   * The initiator can be a user of a system-service or other subjects who could authenticate themselves to work with
   * the system.
   */
  public String getInitiator() {
    return initiator;
  }

  /**
   * If the initiator is a user of the IES, this returns the ID of the user.
   */
  public Optional<Long> getUser() {
    return Optional.ofNullable(this.user);
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
  public final int hashCode() {
    return Objects.hash(
        this.entity, this.initiator, this.user, this.timestamp, this.type, this.comment);
  }

  @Override
  public final boolean equals(Object o) {

    if (!(o instanceof HistoryEntry that)) {
      return false;
    }

    return Objects.equals(this.entity, that.entity)
        && Objects.equals(this.initiator, that.initiator)
        && Objects.equals(this.user, that.user)
        && Objects.equals(this.timestamp, that.timestamp)
        && Objects.equals(this.type, that.type)
        && Objects.equals(this.comment, that.comment);
  }

  @Override
  public String toString() {
    StringBuilder b =
        new StringBuilder(100)
            .append("HistoryEntry[entity:")
            .append(this.entity)
            .append(", initiator:")
            .append(this.initiator)
            .append(", user:")
            .append(this.user)
            .append(", timestamp:")
            .append(this.timestamp)
            .append(", type:")
            .append(this.type)
            .append(", comment:")
            .append(this.comment)
            .append(']');
    return b.toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  public static final class Builder {

    private long entity;
    private String initiator;
    private Long user;
    private long timestamp;
    private HistoryEntryType type;
    private String comment;

    private Builder() {}

    private Builder(HistoryEntry historyEntry) {
      this.entity = historyEntry.entity;
      this.initiator = historyEntry.initiator;
      this.user = historyEntry.user;
      this.timestamp = historyEntry.timestamp;
      this.type = historyEntry.type;
      this.comment = historyEntry.comment;
    }

    public Builder entity(long entity) {
      this.entity = entity;
      return this;
    }

    public Builder initiator(String initiator) {
      Objects.requireNonNull(initiator, "initiator is null");
      this.initiator = initiator;
      return this;
    }

    public Builder user(Long user) {
      this.user = user;
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
