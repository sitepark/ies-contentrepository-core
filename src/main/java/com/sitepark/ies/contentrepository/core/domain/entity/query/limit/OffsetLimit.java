package com.sitepark.ies.contentrepository.core.domain.entity.query.limit;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

@SuppressWarnings(
    "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
public class OffsetLimit implements Limit {

  private final Integer offset;

  private final Integer limit;

  protected OffsetLimit(
      @JsonProperty("offset") Integer offset, @JsonProperty("limit") Integer limit) {
    this.offset = (offset == null) ? Integer.valueOf(0) : offset;
    this.limit = limit;
  }

  public Integer offset() {
    return this.offset;
  }

  public Optional<Integer> limit() {
    return Optional.ofNullable(this.limit);
  }
}
