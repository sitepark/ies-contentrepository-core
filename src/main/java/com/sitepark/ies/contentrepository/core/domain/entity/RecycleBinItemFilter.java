package com.sitepark.ies.contentrepository.core.domain.entity;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public final class RecycleBinItemFilter {

  private final Instant from;

  private final Instant to;

  private final Long user;

  private RecycleBinItemFilter(Builder builder) {
    this.from = builder.from;
    this.to = builder.to;
    this.user = builder.user;
  }

  public Optional<Instant> getFrom() {
    return Optional.ofNullable(this.from);
  }

  public Optional<Instant> getTo() {
    return Optional.ofNullable(this.to);
  }

  public Optional<Long> getUser() {
    return Optional.ofNullable(this.user);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(this.from, this.to, this.user);
  }

  @Override
  public final boolean equals(Object o) {

    if (!(o instanceof RecycleBinItemFilter)) {
      return false;
    }

    RecycleBinItemFilter filter = (RecycleBinItemFilter) o;

    return Objects.equals(this.from, filter.from)
        && Objects.equals(this.to, filter.to)
        && Objects.equals(this.user, filter.user);
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  public static final class Builder {

    private Instant from;

    private Instant to;

    private Long user;

    private Builder() {}

    private Builder(RecycleBinItemFilter recycleBinItemFilter) {
      this.from = recycleBinItemFilter.from;
      this.to = recycleBinItemFilter.to;
      this.user = recycleBinItemFilter.user;
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

    public Builder user(long user) {
      if (user <= 0) {
        throw new IllegalArgumentException("user should be greater then 0");
      }
      this.user = user;
      return this;
    }

    public RecycleBinItemFilter build() {
      return new RecycleBinItemFilter(this);
    }
  }
}
