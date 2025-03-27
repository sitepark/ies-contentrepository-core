package com.sitepark.ies.contentrepository.core.usecase.query.limit;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Optional;

public final class OffsetLimit implements Limit {

  private final Integer offset;

  private final Integer limit;

  public OffsetLimit(@JsonProperty("offset") Integer offset, @JsonProperty("limit") Integer limit) {
    this.offset = (offset == null) ? Integer.valueOf(0) : offset;
    this.limit = limit;
  }

  public Integer getOffset() {
    return this.offset;
  }

  public Optional<Integer> getLimit() {
    return Optional.ofNullable(this.limit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.offset, this.limit);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof OffsetLimit that)) {
      return false;
    }
    return Objects.equals(this.offset, that.offset) && Objects.equals(this.limit, that.limit);
  }

  @Override
  public String toString() {
    return "OffsetLimit [offset=" + offset + ", limit=" + limit + "]";
  }
}
