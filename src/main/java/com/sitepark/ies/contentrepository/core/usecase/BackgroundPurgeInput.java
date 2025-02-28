package com.sitepark.ies.contentrepository.core.usecase;

import com.sitepark.ies.contentrepository.core.domain.entity.query.filter.Filter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.*;

public final class BackgroundPurgeInput {

  private final List<Long> rootList;

  private final Filter filterBy;

  private final boolean forceLock;

  private BackgroundPurgeInput(Builder builder) {
    this.rootList = Collections.unmodifiableList(builder.rootList);
    this.filterBy = builder.filterBy;
    this.forceLock = builder.forceLock;
  }

  @SuppressFBWarnings("EI_EXPOSE_REP")
  public List<Long> getRootList() {
    return this.rootList;
  }

  public Optional<Filter> getFilter() {
    return Optional.ofNullable(this.filterBy);
  }

  public boolean isForceLock() {
    return this.forceLock;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.rootList, this.filterBy, this.forceLock);
  }

  @Override
  public boolean equals(Object o) {

    if (!(o instanceof BackgroundPurgeInput that)) {
      return false;
    }

    return Objects.equals(this.rootList, that.rootList)
        && Objects.equals(this.filterBy, that.filterBy)
        && Objects.equals(this.forceLock, that.forceLock);
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  public static final class Builder {

    private final List<Long> rootList = new ArrayList<>();

    private Filter filterBy;

    private boolean forceLock;

    private Builder() {}

    private Builder(BackgroundPurgeInput backgroundPurgeRequest) {
      this.rootList.addAll(backgroundPurgeRequest.rootList);
      this.filterBy = backgroundPurgeRequest.filterBy;
      this.forceLock = backgroundPurgeRequest.forceLock;
    }

    public Builder rootList(List<Long> rootList) {
      Objects.requireNonNull(rootList, "rootList is null");
      this.rootList.clear();
      for (Long root : rootList) {
        this.root(root);
      }
      return this;
    }

    public Builder root(Long root) {
      Objects.requireNonNull(root, "root is null");
      this.rootList.add(root);
      return this;
    }

    public Builder filterBy(Filter filterBy) {
      Objects.requireNonNull(filterBy, "filterBy is null");
      this.filterBy = filterBy;
      return this;
    }

    public Builder forceLock(boolean forceLock) {
      this.forceLock = forceLock;
      return this;
    }

    public BackgroundPurgeInput build() {
      if (this.rootList.isEmpty() && this.filterBy == null) {
        throw new IllegalStateException("Either rootList or filterBy must be specified");
      }
      return new BackgroundPurgeInput(this);
    }
  }
}
