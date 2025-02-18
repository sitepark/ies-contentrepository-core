package com.sitepark.ies.contentrepository.core.domain.entity.query.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.Objects;

public class And implements Filter {

  @SuppressWarnings(
      "PMD.AvoidFieldNameMatchingTypeName") // so that when deserializing it has the desired format
  private final List<Filter> and;

  protected And(@JsonProperty("and") Filter... and) {
    Objects.requireNonNull(and, "and is null");
    this.and = List.of(and);
  }

  @SuppressFBWarnings("EI_EXPOSE_REP")
  public List<Filter> getAnd() {
    return this.and;
  }
}
