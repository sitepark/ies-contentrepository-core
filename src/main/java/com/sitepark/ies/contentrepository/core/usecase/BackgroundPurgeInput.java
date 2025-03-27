package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.usecase.query.filter.Filter;
import java.util.*;

public final class BackgroundPurgeInput {

  private final Filter filter;

  private final boolean forceLock;

  private BackgroundPurgeInput(Builder builder) {
    this.filter = builder.filter;
    this.forceLock = builder.forceLock;
  }

  public Optional<Filter> getFilter() {
    return Optional.ofNullable(this.filter);
  }

  public boolean isForceLock() {
    return this.forceLock;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.filter, this.forceLock);
  }

  @Override
  public boolean equals(Object o) {

    if (!(o instanceof BackgroundPurgeInput that)) {
      return false;
    }

    return Objects.equals(this.filter, that.filter)
        && Objects.equals(this.forceLock, that.forceLock);
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  public static final class Builder {

    private Filter filter;

    private boolean forceLock;

    private Builder() {}

    private Builder(BackgroundPurgeInput backgroundPurgeRequest) {
      this.filter = backgroundPurgeRequest.filter;
      this.forceLock = backgroundPurgeRequest.forceLock;
    }

    public Builder filter(Filter filter) {
      Objects.requireNonNull(filter, "filter is null");
      this.filter = filter;
      return this;
    }

    public Builder forceLock(boolean forceLock) {
      this.forceLock = forceLock;
      return this;
    }

    public BackgroundPurgeInput build() {
      if (this.filter == null) {
        throw new IllegalStateException("filter must be specified");
      }
      return new BackgroundPurgeInput(this);
    }
  }
}
