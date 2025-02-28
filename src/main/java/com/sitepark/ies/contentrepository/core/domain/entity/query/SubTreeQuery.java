package com.sitepark.ies.contentrepository.core.domain.entity.query;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SubTreeQuery extends Query {

  private final List<Long> rootList;

  protected SubTreeQuery(Builder builder) {
    super(builder);
    this.rootList = Collections.unmodifiableList(builder.rootList);
  }

  @SuppressFBWarnings("EI_EXPOSE_REP")
  public List<Long> getRootList() {
    return this.rootList;
  }

  @Override
  public final int hashCode() {
    return Objects.hash(super.hashCode(), this.rootList);
  }

  @Override
  public final boolean equals(Object o) {

    if (!(o instanceof SubTreeQuery that)) {
      return false;
    }

    return that.canEqual(this) && super.equals(o) && Objects.equals(this.rootList, that.rootList);
  }

  /**
   * @see <a href="https://www.artima.com/articles/how-to-write-an-equality-method-in-java">How to
   *     Write an Equality Method in Java </a>
   */
  @Override
  public boolean canEqual(Object other) {
    return (other instanceof SubTreeQuery);
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Builder toBuilder() {
    return new Builder(this);
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder extends Query.Builder<Builder> {

    private final List<Long> rootList = new ArrayList<>();

    protected Builder() {}

    protected Builder(SubTreeQuery cursorBasedQuery) {
      super(cursorBasedQuery);
      this.rootList.addAll(cursorBasedQuery.rootList);
    }

    public Builder root(Long root) {
      Objects.requireNonNull(root, "root is null");
      this.rootList.add(root);
      return this.self();
    }

    public Builder rootList(List<Long> rootList) {
      Objects.requireNonNull(rootList, "rootList is null");
      this.rootList.clear();
      for (Long root : rootList) {
        this.root(root);
      }
      return this.self();
    }

    @Override
    public SubTreeQuery build() {
      if (this.rootList.isEmpty()) {
        throw new IllegalStateException("rootList must not be empty");
      }
      return new SubTreeQuery(this);
    }

    @Override
    public Builder self() {
      return this;
    }
  }
}
