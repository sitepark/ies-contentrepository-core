package com.sitepark.ies.contentrepository.core.domain.entity;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public final class RecycleBinItemFilter {

  private final Instant from;

  private final Instant to;

  private final String userId;

  private RecycleBinItemFilter(Builder builder) {
    this.from = builder.from;
    this.to = builder.to;
    this.userId = builder.userId;
  }

  public Optional<Instant> getFrom() {
    return Optional.ofNullable(this.from);
  }

  public Optional<Instant> getTo() {
    return Optional.ofNullable(this.to);
  }

  public Optional<String> getUserId() {
    return Optional.ofNullable(this.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.from, this.to, this.userId);
  }

  @Override
  public boolean equals(Object o) {

    if (!(o instanceof RecycleBinItemFilter filter)) {
      return false;
    }

    return Objects.equals(this.from, filter.from)
        && Objects.equals(this.to, filter.to)
        && Objects.equals(this.userId, filter.userId);
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @SuppressWarnings("NullAway.Init") // fields are set via fluent setters and validated in build()
  public static final class Builder {

    private Instant from;

    private Instant to;

    private String userId;

    private Builder() {}

    private Builder(RecycleBinItemFilter recycleBinItemFilter) {
      this.from = recycleBinItemFilter.from;
      this.to = recycleBinItemFilter.to;
      this.userId = recycleBinItemFilter.userId;
    }

    public Builder from(Instant from) {
      Objects.requireNonNull(from, "from is null");
      this.from = from;
      return this;
    }

    public Builder to(Instant to) {
      Objects.requireNonNull(to, "to is null");
      this.to = to;
      return this;
    }

    public Builder userId(String userId) {
      Objects.requireNonNull(userId, "userId is null");
      this.userId = userId;
      return this;
    }

    public RecycleBinItemFilter build() {
      return new RecycleBinItemFilter(this);
    }
  }
}
